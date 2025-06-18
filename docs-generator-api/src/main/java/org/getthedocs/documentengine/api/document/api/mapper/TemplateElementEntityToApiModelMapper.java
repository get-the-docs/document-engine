package org.getthedocs.documentengine.api.document.api.mapper;

import java.util.ArrayList;

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
import java.util.Locale;
import java.util.Map;

import org.getthedocs.documentengine.core.documentstructure.descriptors.TemplateElement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Rest API model mapper for TemplateElement.
 * 
 * @author Levente Ban
 */
@Mapper
public interface TemplateElementEntityToApiModelMapper {

	TemplateElementEntityToApiModelMapper INSTANCE = Mappers.getMapper(TemplateElementEntityToApiModelMapper.class);

	/**
	 * Maps a TemplateElement service model to api model. 
	 * @param source the service model.
	 * @return the api model.
	 */
	default org.getthedocs.documentengine.api.document.api.model.TemplateElement entityToApiModel(
			TemplateElement source) {

		if (source != null) {
			final org.getthedocs.documentengine.api.document.api.model.TemplateElement target = new org.getthedocs.documentengine.api.document.api.model.TemplateElement();

			if (source.getTemplateElementId() != null) {
				target.setTemplateElementId(source.getTemplateElementId().getId());
			}
			target.setTemplateNames(
					TemplateElementEntityToApiModelMapper.INSTANCE.mapTemplateNames(source.getTemplateNames()));
			if (source.getFormat() != null) {
				target.setInputFormat(org.getthedocs.documentengine.api.document.api.model.TemplateElement.InputFormatEnum
						.valueOf(source.getFormat().name()));
			}
			target.setDefaultLocale(map(source.getDefaultLocale()));
			target.setCount(source.getCount());

			return target;
		} else {
			return null;
		}
	}

	/**
	 * TemplateElement service model to API model map.
	 * 
	 * @param source the service model object.
	 * @return the API model object.
	 */
	List<org.getthedocs.documentengine.api.document.api.model.TemplateElement> entityListToApiModelList(
			List<TemplateElement> source);

	/**
	 * Mapper for (Locale,template name) pairs.
	 * 
	 * @param value the template map (locale-string).
	 * @return the api model list (TemplateNameItem).
	 */
	default List<org.getthedocs.documentengine.api.document.api.model.TemplateNameItem> mapTemplateNames(
			Map<Locale, String> value) {

		final List<org.getthedocs.documentengine.api.document.api.model.TemplateNameItem> results = new ArrayList<>(
				value.keySet().size());

		if (value != null) {
			for (final Locale actValue : value.keySet()) {
				final org.getthedocs.documentengine.api.document.api.model.TemplateNameItem actItem = new org.getthedocs.documentengine.api.document.api.model.TemplateNameItem();
				actItem.setLocale(map(actValue));
				actItem.setTemplateName(value.get(actValue));

				results.add(actItem);
			}
		}

		return results;

	}

	/**
	 * Internal locale to string map.
	 * 
	 * @param value the locale value.
	 * @return the result string.
	 */
	default String map(Locale value) {
		return value.toString();
	}

}
