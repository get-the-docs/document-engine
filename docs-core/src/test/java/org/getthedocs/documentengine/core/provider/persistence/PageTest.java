package org.getthedocs.documentengine.core.provider.persistence;

/*-
 * #%L
 * docs-core
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

import org.getthedocs.documentengine.core.template.descriptors.TemplateDocument;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

public class PageTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(PageableTest.class);

  private static final String TL_CONTRACT_FILE_HU = "contract_v09_hu.docx";

  @Test
  public void testDataListOk() {

    try {
      final var p = new Page<TemplateDocument>();
      final var templateDocument = new TemplateDocument(TL_CONTRACT_FILE_HU);

      p.getData().add(templateDocument);

      Assert.assertEquals(1, p.getNumberOfElements().intValue());
    } catch (final Exception e) {
      LOGGER.error("Wrong size caught.", e);

      fail();
    }
  }

  @Test
  public void testDataListEmpty() {

    try {
      final var p = new Page<TemplateDocument>();
      final var templateDocument = new TemplateDocument(TL_CONTRACT_FILE_HU);

      p.getData().add(templateDocument);
      p.setData(null);

      Assert.assertEquals(0, p.getNumberOfElements().intValue());
    } catch (final Exception e) {
      LOGGER.error("Wrong size caught.", e);

      fail();
    }
  }

  @Test
  public void testPropertiesOk() {

    final var p = new Page<TemplateDocument>();
    final var templateDocument = new TemplateDocument(TL_CONTRACT_FILE_HU);

    p.getData().add(templateDocument);
    p.setNumber(5);
    p.setTotalPages(2);

    Assert.assertTrue(1 == p.getNumberOfElements() && p.getNumber() == 5 && p.getTotalPages() == 2);

  }

}
