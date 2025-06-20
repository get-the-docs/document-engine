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

import java.util.List;

import org.getthedocs.documentengine.core.documentstructure.DocumentStructure;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import org.getthedocs.documentengine.core.service.OutputFormat;

/**
 * Document structure service model to API model mapper.
 * 
 * @author Levente Ban
 */
@Mapper
public interface DocumentStructureEntityToApiModelMapper {

	DocumentStructureEntityToApiModelMapper INSTANCE = Mappers.getMapper(DocumentStructureEntityToApiModelMapper.class);

	public static final String DELIMITER = "|";

	/**
	 * Mapper for DocumentStructure service model to the OAS api model. 
	 * @param source the service model object.
	 * @return the api model object.
	 */
	default org.getthedocs.documentengine.api.document.api.model.DocumentStructure entityToApiModel(
			DocumentStructure source) {

			if (source != null) {
				final org.getthedocs.documentengine.api.document.api.model.DocumentStructure target = new org.getthedocs.documentengine.api.document.api.model.DocumentStructure();

				target.setDocumentStructureId(source.getDocumentStructureId());
				if (source.getResultMode() != null) {
					target.setResultMode(org.getthedocs.documentengine.api.document.api.model.DocumentStructure.ResultModeEnum.valueOf(source.getResultMode().name()));
				}
				if (source.getOutputFormat() != null) {
					target.setCopies(source.getCopies());
				}

				return target;
			} else {
				return null;
			}
	}

	/**
	 * Mapper for document structure lists.
	 * @param source the input list.
	 * @return the api model.
	 */
	List<org.getthedocs.documentengine.api.document.api.model.DocumentStructure> entityListToApiModelList(
			List<DocumentStructure> source);

	/**
	 * Internal mapper for locale-string conversion.
	 * @param value the input locale.
	 * @return the string value.
	 */	
	default String map(OutputFormat value) {
		return value.toString();
	}			
}
