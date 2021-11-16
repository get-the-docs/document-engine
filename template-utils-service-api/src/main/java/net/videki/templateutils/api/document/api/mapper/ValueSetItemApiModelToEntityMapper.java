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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import net.videki.templateutils.template.core.context.JsonTemplateContext;

/**
 * Document structure value set API model to service model mapper.
 * 
 * @author Levente Ban
 */
@Mapper
public interface ValueSetItemApiModelToEntityMapper {

	ValueSetItemApiModelToEntityMapper INSTANCE = Mappers.getMapper(ValueSetItemApiModelToEntityMapper.class);

	public static final String DELIMITER = "|";

	/**
	 * Mapper for ValueSetItem service model from the OAS api model. 
	 * @param source the api model object.
	 * @return the service model object.
	 */
	default net.videki.templateutils.api.document.model.ValueSetItem apiModelToEntity(
		net.videki.templateutils.api.document.api.model.ValueSetItem source) {

			if (source != null) {
				final net.videki.templateutils.api.document.model.ValueSetItem target = new net.videki.templateutils.api.document.model.ValueSetItem();

				final Gson gson = new GsonBuilder().setPrettyPrinting().create();
				final String value = gson.toJson(source.getValue());
				
				target.setTemplateElementId(source.getTemplateElementId());
				target.setValue(new JsonTemplateContext(value));

				return target;
			} else {
				return null;
			}
	}

	/**
	 * Mapper for valueSetItem lists.
	 * @param source the input list.
	 * @return the service model.
	 */
	List<net.videki.templateutils.api.document.model.ValueSetItem> apiModelListToEntityList(
			List<net.videki.templateutils.api.document.api.model.ValueSetItem> source);
		
}
