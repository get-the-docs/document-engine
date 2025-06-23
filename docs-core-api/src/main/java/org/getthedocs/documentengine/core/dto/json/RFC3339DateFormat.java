package org.getthedocs.documentengine.core.dto.json;

/*-
 * #%L
 * docs-service-api
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

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.Serial;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * JSON date format config.
 *
 * @author Levente Ban
 */
public class RFC3339DateFormat extends DateFormat {

    /**
     * Serial version UID.
     */
    @Serial
    private static final long serialVersionUID = -3656086267804348961L;

    /**
     * Data timezone.
     */
    private static final TimeZone TIMEZONE_Z = TimeZone.getTimeZone("UTC");

    /**
     * Calendar instance.
     */
    private final Calendar calendar = GregorianCalendar.getInstance();

    /**
     * Date format instance.
     */
    private final StdDateFormat fmt = new StdDateFormat()
            .withTimeZone(TIMEZONE_Z)
            .withColonInTimeZone(true);

    /**
     * Default constructor.
     */
    public RFC3339DateFormat() {
    }

    /**
     * Date parses the input string and converts to date.
     *
     * @param source source string.
     * @param pos    parse position.
     */
    @Override
    public Date parse(String source, ParsePosition pos) {
        return fmt.parse(source, pos);
    }

    /**
     * Date format.
     *
     * @param date          the input date.
     * @param toAppendTo    StringBuffer to append to.
     * @param fieldPosition field position.
     * @return the converted string in a StringBuffer.
     */
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return fmt.format(date, toAppendTo, fieldPosition);
    }
}
