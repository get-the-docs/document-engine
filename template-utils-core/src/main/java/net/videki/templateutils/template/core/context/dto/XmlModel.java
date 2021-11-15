package net.videki.templateutils.template.core.context.dto;

/*-
 * #%L
 * template-utils-core-dto-extensions
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

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public interface XmlModel {

  default String toXml() throws ClassCastException {
    final StringBuilder sb = new StringBuilder();
    try {
      final JAXBContext jaxbContext = JAXBContext.newInstance(this.getClass());
      final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      final StringWriter sw = new StringWriter();
      jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      jaxbMarshaller.marshal(this, sw);

      sb.append(sw.toString());

    } catch (JAXBException | ClassCastException e) {
      throw new ClassCastException("Error converting the xml to text.");
    }

    return sb.toString();
  }
}
