package org.getthedocs.documentengine.core.processor.merger;

/*-
 * #%L
 * docs-core
 * %%
 * Copyright (C) 2021 Levente Ban
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

import org.getthedocs.documentengine.core.service.OutputFormat;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Base interface for document merger implementations.
 * Mergers are implementations to combine documents to a single result document.
 * @author Levente Ban
 */
public interface OutputMerger {

    /**
     * Returns the output format which the merger supports. 
     * @return the output format.
     */
    OutputFormat getOutputFormat();

    /**
     * Entry point for merging a list of documents having the format of the merger output format.
     * @param sources the documents to be processed.
     * @return the result document stream if the merge was successful.
     */
    OutputStream merge(final List<InputStream> sources);
}
