package net.videki.documentengine.api.document.api.mapper;

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

import net.videki.documentengine.core.provider.persistence.Pageable;
import net.videki.documentengine.api.document.api.model.PageableTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Api model mapper for Pageable objects.
 * 
 * @author Levente Ban
 */
@Mapper
public interface PageableMapper {

	PageableMapper INSTANCE = Mappers.getMapper(PageableMapper.class);

	/**
	 * Maps an api model to a service model.
	 * 
	 * @param source the api model.
	 * @return the service model.
	 */
	@Mapping(source = "page", target = "page")
	@Mapping(source = "size", target = "size")
	@Mapping(expression = "java((source.getPage() * source.getSize()) -1 >= 0 ? (source.getPage() * source.getSize()) -1 : 0 )", target = "offset")
	@Mapping(source = "paged", target = "paged")
	@Mapping(source = "sort", target = "sort")
    Pageable map(PageableTemplate source);
/*
	default List<String> map(final org.springframework.data.domain.Sort source) {
		if (source == null) {
			return new LinkedList<>();
		}

		return source.stream().map(Sort.Order::toString).collect(Collectors.toList());
	}
*/
}
