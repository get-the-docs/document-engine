package net.videki.templateutils.template;

import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.videki.templateutils.template.core.util.FileSystemHelper;
import net.videki.templateutils.template.core.context.TemplateContext;
import net.videki.templateutils.template.core.service.TemplateService;
import net.videki.templateutils.template.core.service.TemplateServiceRegistry;
import net.videki.templateutils.template.core.service.exception.TemplateServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.videki.templateutils.template.xlsx.dto.TemplateData;

public class XlsxTemplateTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(XlsxTemplateTest.class);

  private TemplateData generateTestData() {
    TemplateData data = new TemplateData();
    

    Map<String, String> model = data.getData();
    
    return data;
  }

//  @Test
  public void fillXlsxTableRowsResultXlsx() {
    final String inputDir = "/templates";
    final String fileName = "xlsTemplate.xlsx";
    final String projectOutDir = System.getProperty("user.dir") + "/build";
    final String resultFileName = "result-" + fileName;

    final TemplateService ts = TemplateServiceRegistry.getInstance();

    final TemplateData dto = generateTestData();

    final Map<String, Object> dtoExt = new HashMap<>();
    dtoExt.put(TemplateContext.CONTEXT_ROOT_KEY, dto);

    try (final FileOutputStream o = new FileOutputStream(FileSystemHelper.getFullPath(projectOutDir, resultFileName))) {
      final OutputStream result = ts.fill(FileSystemHelper.getFullPath(inputDir, fileName), dtoExt);

      LOGGER.info("Result file: {}/{}.", projectOutDir, resultFileName);
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

}