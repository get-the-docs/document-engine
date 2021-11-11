package net.videki.templateutils.template.core.template.descriptors;

/*-
 * #%L
 * template-utils-core
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

import net.videki.templateutils.template.core.service.InputFormat;
import net.videki.templateutils.template.core.service.exception.TemplateProcessException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

public class TemplateDocumentTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(TemplateDocumentTest.class);

  private static final String TL_CONTRACT_FILE_HU = "contract_v09_hu.docx";
  private static final String TL_CONTRACT_FILE_NOT_SUPPORTED = "contract_v09_hu.docxxxx";

  @Test
  public void inputFormatOk() {

    try {
      final var te = new TemplateDocument(TL_CONTRACT_FILE_HU);

      Assert.assertEquals(InputFormat.DOCX, te.getFormat());
    } catch (final Exception e) {
      LOGGER.error("Wrong format caught.", e);

      fail();
    }
  }

  @Test
  public void inputFormatNonExisting() {

    try {
      new TemplateDocument(TL_CONTRACT_FILE_NOT_SUPPORTED);

      fail();
    } catch (final TemplateProcessException e) {
      Assert.assertEquals("c14d63df-8db2-45a2-bf21-e62fe60a23a0", e.getCode());
    } catch (final Exception e) {
      LOGGER.error("Wrong format caught.", e);

      fail();
    }
  }

}
