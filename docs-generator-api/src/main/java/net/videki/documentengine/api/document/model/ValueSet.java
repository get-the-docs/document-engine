package net.videki.documentengine.api.document.model;

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

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * ValueSet.
 */

@Data
public class ValueSet   {
  private String documentStructureId;
  private String transactionId;
  private String locale;
  private List<ValueSetItem> values = new LinkedList<>();
}

