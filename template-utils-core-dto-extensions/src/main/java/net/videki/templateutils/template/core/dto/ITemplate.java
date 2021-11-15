package net.videki.templateutils.template.core.dto;

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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.apache.commons.lang3.LocaleUtils;
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
import java.time.format.FormatStyle;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Convenience functions for placeholder evaluation result formatting.
 * 
 * @author Levente Ban
 */
public interface ITemplate {

    /** Empty value replacement for forced eval */
    String PLACEHOLDER_EMPTY = "................";

    /** Error marker to indicate formatting errors */
    String PLACEHOLDER_EVALUATION_ERROR = "VALUE_ERROR";

    /** Default date formatter */
    DateTimeFormatter DATE_FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /** Default datetime formatter */
    DateTimeFormatter DATE_FORMAT_DATETIME = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    /** Logger. */
    Logger LOG = LoggerFactory.getLogger(ITemplate.class);

    /**
     * Formats a number according to the default locale's format.
     * 
     * @param value the number value.
     * @return the result value, or the error marker.
     */
    default String fmtN(Integer value) {
        return fmtN((double) value, Locale.getDefault().toString());
    }

    /**
     * Formats a number according to the given locale's format. The value is
     * required, the error marker is returned when missing.
     * 
     * @param value  the number value.
     * @param locale the locale to which to format.
     * @return the result value, or the error marker.
     */
    default String fmtN(Integer value, String locale) {
        return fmtN((double) value, Locale.getDefault().toString());
    }

    /**
     * Formats a number according to the default locale's format.
     * 
     * @param value the number value.
     * @return the result value, or the error marker.
     */
    default String fmtN(Long value) {
        return fmtN(value, Locale.getDefault().toString());
    }

    /**
     * Formats a number according to the given locale's format. The value is
     * required, the error marker is returned when missing.
     * 
     * @param value  the number value.
     * @param locale the locale to which to format.
     * @return the result value, or the error marker.
     */
    default String fmtN(Long value, String locale) {
        try {
            final Locale effectiveLocale = locale != null ? LocaleUtils.toLocale(locale) : Locale.getDefault();

            final DecimalFormatSymbols dfs = new DecimalFormatSymbols(effectiveLocale);

            final DecimalFormat fmt = new DecimalFormat("###,###,###,###,###,###,###,###,##0", dfs);

            final String result = fmt.format(value);

            if (LOG.isTraceEnabled()) {
                LOG.trace("fmtN - value: [{}], locale: [{}] -> [{}]", value, locale, result);
            }
            return result;
        } catch (final IllegalArgumentException e) {
            LOG.warn("fmtN format error: {}, locale: {}", value, locale);

            if (LOG.isDebugEnabled()) {
                LOG.debug("fmtN format error: {}, locale: {}", value, locale, e);
            }

            return PLACEHOLDER_EVALUATION_ERROR;
        }

    }

    /**
     * Formats a number according to the default locale's format.
     * 
     * @param value the number value.
     * @return the result value, or the error marker.
     */
    default String fmtN(Float value) {
        return fmtN(value, Locale.getDefault().toString());
    }

    /**
     * Formats a number according to the given locale's format. The value is
     * required, the error marker is returned when missing.
     * 
     * @param value  the number value.
     * @param locale the locale to which to format.
     * @return the result value, or the error marker.
     */
    default String fmtN(Float value, String locale) {
        return fmtN((double) value, locale);

    }

    /**
     * Formats a number according to the default locale's format.
     * 
     * @param value the number value.
     * @return the result value, or the error marker.
     */
    default String fmtN(Double value) {
        return fmtN(value, Locale.getDefault().toString());
    }

    /**
     * Formats a number according to the given locale's format. The value is
     * required, the error marker is returned when missing.
     * 
     * @param value  the number value.
     * @param locale the locale to which to format.
     * @return the result value, or the error marker.
     */
    default String fmtN(Double value, String locale) {
        try {
            final Locale effectiveLocale = locale != null ? LocaleUtils.toLocale(locale) : Locale.getDefault();

            final DecimalFormatSymbols dfs = new DecimalFormatSymbols(effectiveLocale);

            final DecimalFormat fmt = new DecimalFormat("###,###,###,###,###,###,###,###,##0", dfs);

            final String result = fmt.format(value);

            if (LOG.isTraceEnabled()) {
                LOG.trace("fmtN - value: [{}], locale: [{}] -> [{}]", value, locale, result);
            }
            return result;
        } catch (final IllegalArgumentException e) {
            LOG.warn("fmtN format error: {}, locale: {}", value, locale);

            if (LOG.isDebugEnabled()) {
                LOG.debug("fmtN format error: {}, locale: {}", value, locale, e);
            }

            return PLACEHOLDER_EVALUATION_ERROR;
        }

    }


    /**
     * Formats a number to the given precision according to the default locale's
     * format. The value is required, the error marker is returned when missing.
     * 
     * @param value  the number value.
     * @param m      the required precision.
     * @return the result value, or the error marker.
     */
    default String fmtNxM(Float value, int m) {
        return fmtNxM((double) value, m, Locale.getDefault().toString());
    }
    
    /**
     * Formats a number to the given precision according to the given locale's
     * format. The value is required, the error marker is returned when missing.
     * 
     * @param value  the number value.
     * @param locale the locale to which to format.
     * @param m      the required precision.
     * @return the result value, or the error marker.
     */
    default String fmtNxM(Float value, int m, String locale) {
        return fmtNxM((double) value, m, locale);
    }

    /**
     * Formats a number to the given precision according to the default locale's
     * format. The value is required, the error marker is returned when missing.
     * 
     * @param value  the number value.
     * @param m      the required precision.
     * @return the result value, or the error marker.
     */
    default String fmtNxM(Double value, int m) {
        return fmtNxM(value, m, Locale.getDefault().toString());
    }

    /**
     * Formats a number to the given precision according to the given locale's
     * format. The value is required, the error marker is returned when missing.
     * 
     * @param value  the number value.
     * @param locale the locale to which to format.
     * @param m      the required precision.
     * @return the result value, or the error marker.
     */
    default String fmtNxM(Double value, int m, String locale) {
        try {
            final Locale effectiveLocale = locale != null ? LocaleUtils.toLocale(locale) : Locale.getDefault();

            final DecimalFormatSymbols dfs = new DecimalFormatSymbols(effectiveLocale);

            final DecimalFormat fmt = new DecimalFormat("###,###,##0." + new String(new char[m]).replace('\0', '0'),
                    dfs);

            return fmt.format(value);
        } catch (final IllegalArgumentException e) {
            LOG.warn("fmtN format error: {}, precision: {}, locale: {}", value, m, locale);

            if (LOG.isDebugEnabled()) {
                LOG.debug("fmtN format error: {}, precision: {}, locale: {}", value, m, locale, e);
            }

            return PLACEHOLDER_EVALUATION_ERROR;
        }
    }

    /**
     * Formats a number to the given precision according to the default locale's
     * format. The value is optional, an empty value is returned when missing.
     * 
     * @param value the number value.
     * @param m     the required precision.
     * @return the result value, or the error marker.
     */
    default String fmtNxMOpc(Double value, int m) {
        return fmtNxMOpc(value, m, Locale.getDefault().toString());
    }

    /**
     * Formats a number to the given precision according to the given locale's
     * format. The value is optional, an empty value is returned when missing.
     * 
     * @param value  the number value.
     * @param locale the locale to which to format.
     * @param m      the required precision.
     * @return the result value, or the error marker.
     */
    default String fmtNxMOpc(Double value, int m, String locale) {

        String result;

        if (value != null) {
            result = fmtNxM(value, m, locale);
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

    /**
     * Formats a date to the default format pattern. Attention: the default format
     * is the ITemplate date format pattern, not the locale's one.
     * 
     * @param value the date value.
     * @return the converted value, or the error marker in case of conversion
     *         errors.
     */
    default String fmtDate(LocalDate value) {
        String result;
        if (value != null) {
            result = value.format(DATE_FORMAT_DATE);
        } else {
            result = PLACEHOLDER_EVALUATION_ERROR;
        }
        return result;
    }

    /**
     * Formats a date to the default format pattern.
     * 
     * @param value  the date value.
     * @param locale the locale to be used for formatting.
     * @return the converted value, or the error marker in case of conversion
     *         errors.
     */
    default String fmtDate(LocalDate value, String locale) {

        final Locale effectiveLocale = locale != null ? LocaleUtils.toLocale(locale) : Locale.getDefault();

        String result;
        if (value != null) {
            try {
                final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                        .localizedBy(effectiveLocale);
                result = value.format(df);
            } catch (final Exception e) {
                result = PLACEHOLDER_EVALUATION_ERROR;
            }
        } else {
            result = PLACEHOLDER_EMPTY;
        }
        return result;
    }

    /**
     * Formats a map-represented date (or any object containing a year-month-date
     * property and represented as a map) to the default format pattern.
     * 
     * @param value the date value.
     * @return the converted value, or the error marker in case of conversion
     *         errors.
     */
    default String fmtDate(final Map<?, ?> value) {
        return fmtDate(value, Locale.getDefault().toString());
    }

    /**
     * Formats a map-represented date (or any object containing a year-month-date
     * property and represented as a map) to the default format pattern.
     * 
     * @param value  the date value.
     * @param locale the locale to be used for formatting.
     * @return the converted value, or the error marker in case of conversion
     *         errors.
     */
    default String fmtDate(final Map<?, ?> value, String locale) {
        String result;
        if (value != null) {
            try {
                final Locale effectiveLocale = locale != null ? LocaleUtils.toLocale(locale) : Locale.getDefault();

                final LocalDate d = LocalDate.of((Integer) value.get("year"), (Integer) value.get("month"),
                        (Integer) value.get("day"));

                if (Locale.getDefault().equals(effectiveLocale)) {
                    result = d.format(DATE_FORMAT_DATE);
                } else {
                    final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                            .localizedBy(effectiveLocale);
                    result = d.format(df);
                }
            } catch (final Exception e) {
                result = PLACEHOLDER_EVALUATION_ERROR;
            }
        } else {
            result = PLACEHOLDER_EMPTY;
        }
        return result;
    }

    /**
     * Formats a map-represented date to the default format pattern.
     * 
     * @param value the date value.
     * @return the converted value, or the error marker in case of conversion
     *         errors.
     */
    default String fmtDateTime(LocalDateTime value) {
        return fmtDateTime(value, Locale.getDefault().toString());
    }

    /**
     * Formats a map-represented date to the given locale's format pattern.
     * Attention: the default format is the ITemplate datetime format pattern, not
     * the locale's one.
     * 
     * @param value  the date value.
     * @param locale the locale to be used for formatting.
     * @return the converted value, or the error marker in case of conversion
     *         errors.
     */
    default String fmtDateTime(LocalDateTime value, String locale) {
        String result;
        if (value != null) {
            final Locale effectiveLocale = locale != null ? LocaleUtils.toLocale(locale) : Locale.getDefault();

            if (Locale.getDefault().equals(effectiveLocale)) {
                result = value.format(DATE_FORMAT_DATETIME);
            } else {
                final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        .localizedBy(effectiveLocale);
                result = value.format(df);
            }
        } else {
            result = PLACEHOLDER_EMPTY;
        }
        return result;
    }

    /**
     * Empty string handling.
     * 
     * @param value the value.
     * @return the value, or a blank string (NOT the PLACEHOLDER_EMPTY!).
     */
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

    /**
     * Generates a QR code image for the input string.
     * 
     * @param qrString the string value.
     * @param size     the result image size.
     * @return the qr code image.
     */
    default Image createQRCode(String qrString, int size) {

        final Image docImage;

        if (qrString == null || size <= 0) {
            throw new IllegalArgumentException(
                    String.format("Error generating QR code: invalid params: value: [%s], size: [%d]", qrString, size));
        }

        try {
            final BufferedImage qrImage;

            final Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            hintMap.put(EncodeHintType.MARGIN, 1);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            final QRCodeWriter qrCodeWriter = new QRCodeWriter();
            final BitMatrix byteMatrix = qrCodeWriter.encode(qrString, BarcodeFormat.QR_CODE, 2 * size, 2 * size,
                    hintMap);
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

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
     * Concat vararg based string params.
     * 
     * @param args the string args.
     * @return the result string.
     */
    default String concat(final Object... args) {
        if (args != null && args.length > 0) {
            final StringBuilder sb = new StringBuilder();
            for (final Object actArg : args) {
                sb.append(actArg + " ");
            }

            return sb.toString().replaceAll("\\s$", "");
        } else {
            return PLACEHOLDER_EMPTY;
        }
    }

}
