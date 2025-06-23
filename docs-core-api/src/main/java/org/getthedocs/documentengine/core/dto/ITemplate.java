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

import io.reflectoring.docxstamper.replace.typeresolver.image.Image;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.getthedocs.documentengine.core.dto.json.ObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Interface for pojo template data transfer objects.
 *
 * Provides methods for formatting various data types.
 *
 * @author Levente Ban
 */
public interface ITemplate {

    /**
     * Placeholder for empty string values to show in the result document.
     */
    String PLACEHOLDER_EMPTY = "................";

    /**
     * Placeholder for empty number values to show in the result document.
     */
    String PLACEHOLDER_NUMBER_EMPTY = "";

    /**
     * Date format for date values.
     */
    DateTimeFormatter DATE_FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /**
     * Date format for date and time values.
     */
    DateTimeFormatter DATE_FORMAT_DATETIME = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    /**
     * Default logger for the formatters.
     */
    Logger LOG = LoggerFactory.getLogger(ITemplate.class);

    /**
     * Formats an integer value into a localized string representation.
     *
     * @param value the integer value to format
     * @return the formatted string or a placeholder if formatting fails and logs the warning
     */
    default String fmtN(final Integer value) {
        try {
            final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
            dfs.setDecimalSeparator(',');
            dfs.setGroupingSeparator(' ');

            final DecimalFormat fmt = new DecimalFormat("###,###,##0", dfs);
            return fmt.format(value);
        } catch (final IllegalArgumentException e) {
            LOG.warn("fmtN format error: {}", value, e);

            return PLACEHOLDER_EMPTY;
        }

    }

    /**
     * Formats a double value into a localized string representation with a specified number of decimal places.
     *
     * @param value the double value to format
     * @param m     the number of decimal places
     * @return the formatted string or a placeholder if formatting fails and logs the warning
     */
    default String fmtNxM(final Double value, final int m) {
        try {
            final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
            dfs.setDecimalSeparator(',');
            dfs.setGroupingSeparator(' ');

            final DecimalFormat fmt = new DecimalFormat(m > 0 ? "###,###,###,###,###,###,##0." + new String(new char[m]).replace('\0', '0') : "###,###,###,###,###,###,##0", dfs);

            return fmt.format(value);
        } catch (final IllegalArgumentException e) {
            LOG.warn("fmtNxM format error: {}", value, e);

            return PLACEHOLDER_NUMBER_EMPTY;
        }
    }

    /**
     * Formats an optional double value into a localized string representation with a specified number of decimal places.
     *
     * @param value the double value to format
     * @param m     the number of decimal places
     * @return the formatted string or a placeholder if formatting fails
     */
    default String fmtNxMOpc(final Double value, final int m) {

        String result;

        if (value != null) {
            result = fmtNxM(value, m);
        } else {
            result = PLACEHOLDER_NUMBER_EMPTY;
        }

        return result;
    }

    /**
     * Only for compatibility reason to avoid data type issues on context objects.
     *
     * @param value the string value to format
     * @return the input value as a string
     */
    default String fmtN(final String value) {
        return value;
    }

    /**
     * Only for compatibility reason to avoid data type issues on context objects.
     *
     * @param value the string value to format
     * @return the input value as a string
     */
    default String fmtNxM(final String value) {
        return fmtNxM(Double.valueOf(value), 0);
    }

    /**
     * Formats an optional number as a string into a localized string representation.
     *
     * @param value the string value to format
     * @return the formatted string or a placeholder if formatting fails
     */
    default String fmtNxMOpc(final String value) {

        String result;

        if (value != null) {
            result = fmtNxM(value);
        } else {
            result = PLACEHOLDER_NUMBER_EMPTY;
        }

        return result;
    }

    /**
     * Formats a date value into a localized string representation.
     *
     * @param value the date value to format
     * @return the formatted string or a placeholder if formatting fails and logs the warning
     */
    default String fmtDate(final LocalDate value) {
        String result;
        if (value != null) {
            result = value.format(DATE_FORMAT_DATE);
        } else {
            result = PLACEHOLDER_EMPTY;
        }
        return result;
    }

    /**
     * Formats a date value from a year-month-date map representation into a localized string representation.
     *
     * @param value the map containing the date components
     * @return the formatted string or a placeholder if formatting fails and logs the warning
     */
    default String fmtDate(final Map<?, ?> value) {
        String result;
        if (value != null) {
            final ObjectMapper mapper = ObjectMapperFactory.jsonMapper();
            LocalDate d = mapper.convertValue(value.values().toArray(), LocalDate.class);
            result = d.format(DATE_FORMAT_DATE);
        } else {
            result = PLACEHOLDER_EMPTY;
        }
        return result;
    }

    /**
     * Formats a date and time value into a localized string representation.
     *
     * @param value the date and time value to format
     * @return the formatted string or a placeholder if formatting fails and logs the warning
     */
    default String fmtDateTime(final LocalDateTime value) {
        String result;
        if (value != null) {
            result = value.format(DATE_FORMAT_DATETIME);
        } else {
            result = PLACEHOLDER_EMPTY;
        }
        return result;
    }

    /**
     * Formats a date and time value from a year-month-date-hour-minute-second map representation into a localized string representation.
     *
     * @param value the map containing the date and time components
     * @return the formatted string or a placeholder if formatting fails and logs the warning
     */
      default String optional(final String value) {
        return value != null ? value : "";
      }

    /**
     * Collates a list of items into columns based on a specified column count.
     * @param items     the list of items to collate
     * @param colCount the number of columns to collate the items into
     * @return the collated list of items in columns
     * @param <T> the type of items in the list
     */
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

    /**
     * Creates a QR code image from the provided string.
     * @param qrString the string to encode in the QR code
     * @param size the size of the QR code image in pixels
     * @return the QR code image
     */
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

            final Graphics2D graphics = (Graphics2D) qrImage.getGraphics();
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

    /**
     * Concatenates the values of a map into a single string.
     * @param args the map containing the values to concatenate
     * @return the concatenated string
     */
    default String concat(final Map<String, Object> args) {
        return args.values()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }

}
