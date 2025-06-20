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

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.getthedocs.documentengine.core.template.descriptors.TemplateDocument;
import org.getthedocs.documentengine.api.document.api.model.GetTemplatesResponse;
import org.getthedocs.documentengine.api.document.api.model.Page;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Api response mapper: GetTemplatesResponse.
 * 
 * @author Levente Ban
 */
@Mapper
public interface GetTemplatesResponseApiModelMapper {

	GetTemplatesResponseApiModelMapper INSTANCE = Mappers.getMapper(GetTemplatesResponseApiModelMapper.class);

	/**
	 * Maps a service page model to an api page.
	 * @param source the input page.
	 * @return the result page.
	 */
	default GetTemplatesResponse pageToApiModel(org.getthedocs.documentengine.core.provider.persistence.Page<TemplateDocument> source) {

		final GetTemplatesResponse result = new GetTemplatesResponse();
		final Page page = new Page();
		page.setTotalPages(source.getTotalPages());
		page.totalElements(source.getTotalElements());
		page.setNumber(source.getNumber());
		page.setSize(source.getSize());
		page.setNumberOfElements(source.getNumberOfElements());
		result.setPage(page);

		result.setContents(new ArrayList<org.getthedocs.documentengine.api.document.api.model.TemplateDocument>(source.getData().stream().map(TemplateDocumentToApiModelMapper.INSTANCE::entityToApiModel).collect(Collectors.toList())));

		return result;
	}

}

