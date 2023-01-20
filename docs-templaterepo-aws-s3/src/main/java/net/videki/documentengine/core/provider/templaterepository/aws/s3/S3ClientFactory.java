package net.videki.documentengine.core.provider.templaterepository.aws.s3;

/*-
 * #%L
 * docs-templaterepo-aws-s3
 * %%
 * Copyright (C) 2021 - 2023 Levente Ban
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

@Slf4j
public class S3ClientFactory {

    /**
     * Returns the bucket region for a bucket.
     *
     * @param bucketName The S3 bucket name.
     * @return The AWS region of the bucket if the bucket exists.
     * @throws IllegalArgumentException thrown if the bucket is not present.
     */
    public static Region getRegion(final String bucketName) throws IllegalArgumentException{
        try (S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_2)
                .build()) {

            try {
                final GetBucketLocationResponse response = s3.getBucketLocation(GetBucketLocationRequest.builder()
                        .bucket(bucketName)
                        .build());

                return Region.of(response.locationConstraint().toString());
            } catch (final NoSuchBucketException | NoSuchKeyException e) {
                final String msg = "Bucket not found.";
                log.trace(msg, e);

                throw new IllegalArgumentException(msg);
            } catch (final S3Exception e) {
                final String msg = "Unhandled object store (S3) error.";
                log.trace(msg, e);

                throw new IllegalArgumentException(msg);
            }
        }

    }

    /**
     * Returns an S3Client having the us-east-2 as default region.
     * @return The S3Client object.
     */
    protected static S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_2)
                .build();

    }

    /**
     * Returns an S3Client object initialized for the given region.
     * @param region The required AWS region.
     * @return The S3Client object.
     * @throws IllegalArgumentException thrown if the region cannot be found or other storage access errors.
     */
    public static S3Client getS3Client(final Region region) {
        try {
            return S3Client.builder()
                    .region(region)
                    .build();

        } catch (final NoSuchBucketException | NoSuchKeyException e) {
            final String msg = "Object not found.";
            log.trace(msg, e);

            throw new IllegalArgumentException(msg);
        } catch (final S3Exception e) {
            final String msg = "Unhandled object store (S3) error.";
            log.trace(msg, e);

            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Returns an S3Client in the given bucket's region. (Operations can be made through the regional endpoints)
     * @param bucketName The bucket name.
     * @return The S3Client object.
     * @throws IllegalArgumentException thrown if the bucket cannot be found or other storage access errors.
     */
    public static S3Client getS3Client(final String bucketName) {
        try {
            return S3Client.builder()
                    .region(getRegion(bucketName))
                    .serviceConfiguration(S3Configuration.builder()
                            .accelerateModeEnabled(isTransferAccelerationEnabledForBucket(bucketName))
                            .build())
                    .build();

        } catch (final IllegalArgumentException | NoSuchBucketException | NoSuchKeyException e) {
            final String msg = "Bucket not found.";
            log.trace(msg, e);

            throw new IllegalArgumentException(msg);
        } catch (final S3Exception e) {
            final String msg = "Unhandled object store (S3) error.";
            log.trace(msg, e);

            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Checks whether transfer acceleration is enabled for the given bucket.
     * @param bucketName The bucket name.
     * @return The S3Client object.
     */
    private static boolean isTransferAccelerationEnabledForBucket(final String bucketName) {
        try (S3Client s3 = S3Client.builder()
                .region(getRegion(bucketName))
                .build()) {

            try {
                final GetBucketAccelerateConfigurationResponse r = s3.getBucketAccelerateConfiguration(
                        GetBucketAccelerateConfigurationRequest.builder()
                                .bucket(bucketName)
                                .build());
                return BucketAccelerateStatus.ENABLED.equals(r.status());
            } catch (final S3Exception e) {
                final IllegalArgumentException ex =
                        new IllegalArgumentException("Error querying bucket parameters.");
                ex.setStackTrace(e.getStackTrace());

                throw ex;
            }
        }
    }


}
