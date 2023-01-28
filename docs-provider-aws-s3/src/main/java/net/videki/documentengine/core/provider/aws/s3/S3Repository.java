package net.videki.documentengine.core.provider.aws.s3;

/*-
 * #%L
 * docs-provider-aws-s3
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

public interface S3Repository {

    /**
     * Returns the AWS bucket name used by the repository.
     * @return The bucket name.
     */
    String getBucketName();

    /**
     * Returns the basedir used by the repository.
     * @return The basedir.
     */
    String getPrefix();
}
