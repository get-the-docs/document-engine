package net.videki.templateutils.api.document.api.mapper;

/*-
 * #%L
 * template-utils-service-api
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

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import net.videki.templateutils.template.core.service.OutputFormat;

@Mapper
public interface DocumentStructureEntityToApiModelMapper {

	DocumentStructureEntityToApiModelMapper INSTANCE = Mappers.getMapper(DocumentStructureEntityToApiModelMapper.class);

	public static final String DELIMITER = "|";

	default net.videki.templateutils.api.document.api.model.DocumentStructure entityToApiModel(
			net.videki.templateutils.template.core.documentstructure.DocumentStructure source) {

			if (source != null) {
				final net.videki.templateutils.api.document.api.model.DocumentStructure target = new net.videki.templateutils.api.document.api.model.DocumentStructure();

				target.setDocumentStructureId(source.getDocumentStructureId());
				if (source.getResultMode() != null) {
					target.setResultMode(net.videki.templateutils.api.document.api.model.DocumentStructure.ResultModeEnum.valueOf(source.getResultMode().name()));
				}
				if (source.getOutputFormat() != null) {
					target.setCopies(source.getCopies());
				}

				return target;
			} else {
				return null;
			}
	}

	List<net.videki.templateutils.api.document.api.model.DocumentStructure> entityListToApiModelList(
			List<net.videki.templateutils.template.core.documentstructure.DocumentStructure> source);

	
	
	default String map(OutputFormat value) {
		return value.toString();
	}			
}
