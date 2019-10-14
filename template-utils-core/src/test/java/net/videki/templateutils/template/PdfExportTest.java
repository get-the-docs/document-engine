package net.videki.templateutils.template;

import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.videki.templateutils.template.core.util.FileSystemHelper;
import net.videki.templateutils.template.core.service.OutputFormat;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfExportTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(PdfExportTest.class);

  //@Test
  public void docxToPdf() {
    final String inputDir = "/templates";
    final String fileName = "fillDocxTableRows.docx";
    final String projectOutDir = System.getProperty("user.dir") + "/build";
    final String resultFileName = "result-" + fileName.replace(".docx", ".pdf");

    TemplateService ts = TemplateServiceRegistry.getInstance();

    TableDTO dto = new TableDTO();

    try {
      OutputStream result = ts.fill(FileSystemHelper.getFullPath(inputDir, fileName), dto, OutputFormat.PDF);

      LOGGER.info("Result file: {}/{}.", projectOutDir, resultFileName);

      FileOutputStream o = new FileOutputStream(FileSystemHelper.getFullPath(projectOutDir, resultFileName));

      o.write(((ByteArrayOutputStream)result).toByteArray());
      o.flush();
      o.close();
      result.close();

      LOGGER.info("Done.");
    } catch (IOException e) {
      System.out.println("error:");
      e.printStackTrace();
    } catch (TemplateServiceException e) {
      e.printStackTrace();
    }

    assertTrue(true);
  }

//  @Test
//  public void multipleDocxToMergedPdf() {
//    final String inputDir = "/net/videki/template-utils/template/impl/processor";
//    final String fileName = "fillDocxTableRows.docx";
//    final String fileName2 = "fillDocxTableRows2.docx";
//    final String projectOutDir = System.getProperty("user.dir");
//    final String resultFileName = "mergedResult-" + fileName.replace(".docx", ".pdf");
//
//    TemplateService ts = new TemplateServiceImpl();
//
//    TableDTO dto = new TableDTO();
//    TableDTO dto2 = new TableDTO();
//    dto2.getRowdata().add(new TableRowDTO("dto2Name4", "dto2Actor4"));
//
//    FillDescriptor fillDescriptor1 = new FillDescriptor(inputDir + "/" + fileName);
//    fillDescriptor1.getValueList().add(dto);
//
//    FillDescriptor fillDescriptor2 = new FillDescriptor(inputDir + "/" + fileName2);
//    fillDescriptor2.getValueList().add(dto2);
//
//    List<FillDescriptor> dList = new LinkedList<>();
//    dList.add(fillDescriptor1);
//    dList.add(fillDescriptor2);
//
//    OutputStream result = ts.fillAndMerge(dList, OutputFormat.PDF);
//
//    try {
//      LOGGER.info("Result file: {}/{}.", projectOutDir, resultFileName);
//
//      FileOutputStream o = new FileOutputStream(projectOutDir + "/" + resultFileName);
//
//      o.write(((ByteArrayOutputStream)result).toByteArray());
//      o.flush();
//      o.close();
//      result.close();
//
//      LOGGER.info("Done.");
//    } catch (IOException e) {
//      System.out.println("error:");
//      e.printStackTrace();
//    }
//
//    assertTrue(true);
//  }

}