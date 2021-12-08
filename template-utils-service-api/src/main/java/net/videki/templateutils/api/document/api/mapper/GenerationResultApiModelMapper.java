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

import net.videki.templateutils.api.document.api.model.GenerationResult;
import net.videki.templateutils.template.core.documentstructure.v1.StoredGenerationResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

/**
 * API response mapper: GenerationResult.
 * 
 * @author Levente Ban
 */
@Mapper
public interface GenerationResultApiModelMapper {

	GenerationResultApiModelMapper INSTANCE = Mappers.getMapper(GenerationResultApiModelMapper.class);

	/**
	 * Maps a document structure page.
	 * @param source the input page.
	 * @return the api model.
	 */
	default GenerationResult map(
			StoredGenerationResult source) {
		GenerationResult result = null;

		if (source != null) {
			result = new GenerationResult();

			result.setTransactionId(source.getTransactionId());
//			result.setValuesetTransactionId(source.getValueSetTransactionId());
			if (source.getResults() != null) {
				result.setElements(
						source.getResults().stream().map(StoredResultDocumentApiModelMapper.INSTANCE::map).collect(Collectors.toList()));
			}
		}

		return result;
	}

}
