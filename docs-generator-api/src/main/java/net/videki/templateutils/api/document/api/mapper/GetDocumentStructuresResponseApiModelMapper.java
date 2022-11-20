package net.videki.templateutils.api.document.api.mapper;

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

import java.util.ArrayList;
import java.util.stream.Collectors;

import net.videki.templateutils.api.document.api.model.GetDocumentStructuresResponse;
import net.videki.templateutils.api.document.api.model.Page;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * API response mapper: GetDocumentStructuresResponse.
 * 
 * @author Levente Ban
 */
@Mapper
public interface GetDocumentStructuresResponseApiModelMapper {

	GetDocumentStructuresResponseApiModelMapper INSTANCE = Mappers.getMapper(GetDocumentStructuresResponseApiModelMapper.class);

	/**
	 * Maps a document structure page.
	 * @param source the input page.
	 * @return the api model.
	 */
	default GetDocumentStructuresResponse pageToApiModel(
			net.videki.templateutils.template.core.provider.persistence.Page<net.videki.templateutils.template.core.documentstructure.DocumentStructure> source) {

		final GetDocumentStructuresResponse result = new GetDocumentStructuresResponse();
		final Page page = new Page();
		page.setTotalPages(source.getTotalPages());
		page.totalElements(source.getTotalElements());
		page.setNumber(source.getNumber());
		page.setSize(source.getSize());
		page.setNumberOfElements(source.getNumberOfElements());
		result.setPage(page);

		result.setContents(
				new ArrayList<net.videki.templateutils.api.document.api.model.DocumentStructure>(source.getData().stream()
						.map(DocumentStructureEntityToApiModelMapper.INSTANCE::entityToApiModel).collect(Collectors.toList())));

		return result;
	}

}
