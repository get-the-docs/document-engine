package net.videki.templateutils.template.core.dto;

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
