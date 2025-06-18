package org.getthedocs.documentengine.core.dto;

/*-
 * #%L
 * docs-core-dto-extensions
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wickedsource.docxstamper.replace.typeresolver.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public interface ITemplate {
  Locale LC_HU = new Locale("hu", "HU");
  String PLACEHOLDER_EMPTY = "................";
  DateTimeFormatter DATE_FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");
  DateTimeFormatter DATE_FORMAT_DATETIME = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

  Logger           LOG             = LoggerFactory.getLogger(ITemplate.class);

  default String fmtN(final Integer value) {
    try {
      final DecimalFormatSymbols dfs = new DecimalFormatSymbols(LC_HU);
      dfs.setDecimalSeparator(',');
      dfs.setGroupingSeparator(' ');

      final DecimalFormat fmt = new DecimalFormat("###,###,##0", dfs);
      return fmt.format(value);
    } catch (final IllegalArgumentException e) {
      LOG.warn("fmtN format error: {}", value, e);

      return PLACEHOLDER_EMPTY;
    }

  }

  default String fmtNxM(final Double value, final int m) {
    try {
      final DecimalFormatSymbols dfs = new DecimalFormatSymbols(LC_HU);
      dfs.setDecimalSeparator(',');
      dfs.setGroupingSeparator(' ');

      final DecimalFormat fmt = new DecimalFormat("###,###,##0." + new String(new char[m]).replace('\0', '0'), dfs);

      return fmt.format(value);
    } catch (final IllegalArgumentException e) {
      LOG.warn("fmtNxM format error: {}", value, e);

      return "";
    }
  }

  default String fmtNxMOpc(final Double value, final int m) {

    String result;

    if (value != null) {
      result = fmtNxM(value, m);
    } else {
      result = "";
    }

    return result;
  }

  default String fmtN(final String value) {
    return value;
  }

  default String fmtNxM(final String value) {
    return value;
  }

  default String fmtNxMOpc(final String value) {

    String result;

    if (value != null) {
      result = fmtNxM(value);
    } else {
      result = "";
    }

    return result;
  }

  default String fmtDate(final LocalDate value) {
    String result;
    if (value != null) {
      result = value.format(DATE_FORMAT_DATE);
    } else {
      result = PLACEHOLDER_EMPTY;
    }
    return result;
  }

  default String fmtDate(final Map<?, ?> value) {
    String result;
    if (value != null) {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      //LocalDate.of((Integer) value.get("year"), (Integer) value.get("month"), (Integer) value.get("day"))
      LocalDate d = mapper.convertValue(value.values().toArray(), LocalDate.class);
      result = d.format(DATE_FORMAT_DATE);
    } else {
      result = PLACEHOLDER_EMPTY;
    }
    return result;
  }

  default String fmtDateTime(final LocalDateTime value) {
    String result;
    if (value != null) {
      result = value.format(DATE_FORMAT_DATETIME);
    } else {
      result = PLACEHOLDER_EMPTY;
    }
    return result;
  }

  default String optional(final String value) {
    return value != null ? value : "";
  }

  default <T> List<List<T>> collateToColumns(final List<T> items, final int colCount) {
    final List<List<T>> results = new LinkedList<>();

    if (colCount > 1 && items != null && !items.isEmpty()) {

      List<T> actRow = new ArrayList<>(colCount);

      for (int i = 0; i < items.size(); i++) {

        if ((i % colCount) == 0) {
          actRow = new ArrayList<>(colCount);
          results.add(actRow);
        }

        actRow.add(items.get(i));

      }

    }

    return results;
  }

  default Image createQRCode(final String qrString, final int size) {

    BufferedImage qrImage;
    Image docImage;
    try {
      final Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
      hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

      hintMap.put(EncodeHintType.MARGIN, 1);
      hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

      final QRCodeWriter qrCodeWriter = new QRCodeWriter();
      final BitMatrix byteMatrix = qrCodeWriter.encode(qrString,
              BarcodeFormat.QR_CODE, 2 * size, 2 * size, hintMap);
      final int crunchifyWidth = byteMatrix.getWidth();
      qrImage = new BufferedImage(crunchifyWidth, crunchifyWidth, BufferedImage.TYPE_INT_RGB);
      qrImage.createGraphics();

      final Graphics2D graphics = (Graphics2D)qrImage.getGraphics();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, crunchifyWidth, crunchifyWidth);
      graphics.setColor(Color.BLACK);

      for (int i = 0; i < crunchifyWidth; i++) {
        for (int j = 0; j < crunchifyWidth; j++) {
          if (byteMatrix.get(i, j)) {
            graphics.fillRect(i, j, 1, 1);
          }
        }
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(qrImage, "jpg", baos);
      baos.flush();
      byte[] imageInByte = baos.toByteArray();
      baos.close();

      docImage = new Image(imageInByte);

    } catch (final WriterException | IOException e) {
      throw new RuntimeException("Error generating QR code: " + e.getMessage(), e);
    }

    return docImage;
  }

  default String concat(final Map<String, Object> args) {
    return args.values()
            .stream()
            .map(Object::toString)
            .collect(Collectors.joining(" "));
  }

}
