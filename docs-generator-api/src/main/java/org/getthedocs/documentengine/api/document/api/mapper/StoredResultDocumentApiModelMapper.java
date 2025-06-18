package org.getthedocs.documentengine.api.document.api.mapper;

/*-
 * #%L
 * docs-service-api
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

import org.getthedocs.documentengine.core.documentstructure.StoredResultDocument;
import org.getthedocs.documentengine.api.document.api.model.ResultDocument;
import org.getthedocs.documentengine.core.documentstructure.descriptors.StoredResultDocumentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * API response mapper: ResultDocument.
 * 
 * @author Levente Ban
 */
@Mapper
public interface StoredResultDocumentApiModelMapper {

	StoredResultDocumentApiModelMapper INSTANCE = Mappers.getMapper(StoredResultDocumentApiModelMapper.class);

	/**
	 * Maps a document structure page.
	 * @param source the input page.
	 * @return the api model.
	 */
	@Mapping(source = "transactionId", target = "transactionId")
	@Mapping(source = "fileName", target = "documentName")
	@Mapping(source = "status", target = "status")
	ResultDocument map(
			StoredResultDocument source);

	default String map(final StoredResultDocumentStatus source) {
		if (source != null) {
			return source.name();
		} else {
			return null;
		}
	}
}
