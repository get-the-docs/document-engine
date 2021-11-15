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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;

import org.junit.Test;

public class ITemplateTest {

    @Test
    public void collectToColumns() {
    }
    
    @Test
    public void concatValidShouldReturnConcatenatedValues() {
        final ITemplate ctx = new ITemplate() {};

        assertEquals("one two three", ctx.concat("one", "two", "three"));
    }

    
    @Test
    public void concatNoParamsShouldReturnEmpty() {
        final ITemplate ctx = new ITemplate() {};

        assertEquals(ITemplate.PLACEHOLDER_EMPTY, ctx.concat((Object[]) null));
    }
    
    @Test
    public void qrCodeValidShoudlReturnImage() {
        final ITemplate ctx = new ITemplate() {};

        assertNotNull(ctx.createQRCode(this.getClass().getCanonicalName(), 32));
    }

    @Test(expected = IllegalArgumentException.class)
    public void qrCodeNullValueShoudlThrowRTException() {
        final ITemplate ctx = new ITemplate() {};

        ctx.createQRCode(null, 32);

        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void qrCodeNullSizeShoudlThrowRTException() {
        final ITemplate ctx = new ITemplate() {};

        ctx.createQRCode("blahblah", 0);

        fail();
    }

    @Test
    public void fmtDateValidShouldBeFormattedToITemplateDefault() {
        final ITemplate ctx = new ITemplate() {};

        final var d = LocalDate.of(1970, Month.JANUARY, 1);

        assertEquals(d.format(ITemplate.DATE_FORMAT_DATE), ctx.fmtDate(d));

    }

    @Test
    public void fmtDateValidWithLocaleShouldBeFormattedWithLocaleMedium() {
        final ITemplate ctx = new ITemplate() {};

        final var d = LocalDate.of(1970, Month.JANUARY, 1);
        final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).localizedBy(Locale.CANADA);

        assertEquals(d.format(df), ctx.fmtDate(d, Locale.CANADA.toString()));

    }

    @Test
    public void fmtDateMapValidShouldBeFormattedWithLocaleMedium() {
        final ITemplate ctx = new ITemplate() {};

        final var d = LocalDate.of(1970, Month.JANUARY, 1);
        final var dm = new HashMap<String, Object>();
        dm.put("year", 1970);
        dm.put("month", 1);
        dm.put("day", 1);

        assertEquals(d.format(ITemplate.DATE_FORMAT_DATE), ctx.fmtDate(dm));

    }
    
    @Test
    public void fmtDateMapValidWithLocaleShouldBeFormattedWithLocaleMedium() {
        final ITemplate ctx = new ITemplate() {};

        final var d = LocalDate.of(1970, Month.JANUARY, 1);
        final var dm = new HashMap<String, Object>();
        dm.put("year", 1970);
        dm.put("month", 1);
        dm.put("day", 1);

        final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).localizedBy(Locale.CANADA);

        assertEquals(d.format(df), ctx.fmtDate(dm, Locale.CANADA.toString()));

    }
    
    @Test
    public void fmtDateTime() {
        final ITemplate ctx = new ITemplate() {};

        final var d = LocalDateTime.of(1970, Month.JANUARY, 1, 8, 5, 11);

        assertEquals(d.format(ITemplate.DATE_FORMAT_DATETIME), ctx.fmtDateTime(d));
    }

    @Test
    public void fmtDateTimeWithLocale() {
        final ITemplate ctx = new ITemplate() {};

        final var d = LocalDateTime.of(1970, Month.JANUARY, 1, 8, 5, 11);
        final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(Locale.CANADA);

        assertEquals(d.format(df), ctx.fmtDateTime(d, Locale.CANADA.toString()));
    }

    @Test
    public void fmtNIntValue() {
        final ITemplate ctx = new ITemplate() {};

        final int value = Integer.MAX_VALUE;

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        final DecimalFormat fmt = new DecimalFormat("###,###,##0", dfs);
        
        assertEquals(fmt.format(value), ctx.fmtN(value));
    }

    @Test
    public void fmtNLongValue() {
        final ITemplate ctx = new ITemplate() {};

        final long value = Long.MAX_VALUE;

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        final DecimalFormat fmt = new DecimalFormat("###,###,###,###,###,###,##0", dfs);
        
        assertEquals(fmt.format(value), ctx.fmtN(value));
    }

    @Test
    public void fmtNFloatValue() {
        final ITemplate ctx = new ITemplate() {};

        final Float value = Float.MAX_VALUE;

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        final DecimalFormat fmt = new DecimalFormat("###,###,###,###,###,###,##0", dfs);
        
        assertEquals(fmt.format(value), ctx.fmtN(value));
    }

    @Test
    public void fmtNDoubleValue() {
        final ITemplate ctx = new ITemplate() {};

        final double value = Long.MAX_VALUE;

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        final DecimalFormat fmt = new DecimalFormat("###,###,###,###,###,###,##0", dfs);
        
        assertEquals(fmt.format(value), ctx.fmtN(value));
    }


    @Test
    public void fmtNNullValueShouldReturnEvalErrorPlaceholder() {
        final ITemplate ctx = new ITemplate() {};

        final Double value = null;
        
        assertEquals(ITemplate.PLACEHOLDER_EVALUATION_ERROR, ctx.fmtN(value));
    }
    
    @Test
    public void fmtNxM() {
        final ITemplate ctx = new ITemplate() {};

        final float value = 12345.6789f;

        final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        
        assertEquals("12" + dfs.getGroupingSeparator() + "345" + dfs.getDecimalSeparator() + "68", ctx.fmtNxM(value, 2));
    }

}
