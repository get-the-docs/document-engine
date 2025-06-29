package org.getthedocs.documentengine.core.dto;

/*-
 * #%L
 * docs-core-dto
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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

/**
 * Pojo data object marker to indicate the object is an XML model.
 * <p>
 * This interface provides a method to convert the implementing class to an XML string representation.
 */
public interface XmlModel {

    /**
     * Converts the implementing class to an XML string representation.
     *
     * @return The XML string representation of the object.
     * @throws ClassCastException If an error occurs during the conversion process.
     */
    default String toXml() throws ClassCastException {
        final StringBuilder sb = new StringBuilder();
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(this.getClass());
            final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            final StringWriter sw = new StringWriter();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(this, sw);

            sb.append(sw);

        } catch (JAXBException | ClassCastException e) {
            throw new ClassCastException("Error converting the xml to text.");
        }

        return sb.toString();
    }
}
