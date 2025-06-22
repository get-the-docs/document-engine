package org.getthedocs.documentengine.core.dto;

/*-
 * #%L
 * docs-core-dto
 * %%
 * Copyright (C) 2023 - 2025 Levente Ban
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ITemplateTest {

    @Test
    void fmtN() {
        ITemplate template = new ITemplate() {
        };

        // Test case: Correct formatting of a positive integer
        assertEquals("12 345", template.fmtN(12345));

        // Test case: Correct formatting of a negative integer
        assertEquals("-12 345", template.fmtN(-12345));

        // Test case: Null input should return PLACEHOLDER_EMPTY
        assertEquals(ITemplate.PLACEHOLDER_EMPTY, template.fmtN((Integer) null));

    }

    @Test
    void fmtN_shouldFormatNumberCorrectly() {
        ITemplate template = new ITemplate() {
        };
        String result = template.fmtN(123456);
        assertEquals("123 456", result);
    }

    @Test
    void fmtN_shouldReturnPlaceholderForInvalidFormatting() {
        ITemplate template = new ITemplate() {
        };
        String result = template.fmtN((Integer) null);
        assertEquals(ITemplate.PLACEHOLDER_EMPTY, result);
    }

    @Test
    void fmtNxM_shouldReturnPlaceholderForInvalidFormatting() {
        ITemplate template = new ITemplate() {
        };
        String result = template.fmtNxM(null, 2);
        assertEquals(ITemplate.PLACEHOLDER_NUMBER_EMPTY, result);
    }

    @Test
    void fmtNxM_shouldReturnPlaceholderForNullWithZeroDecimals() {
        ITemplate template = new ITemplate() {
        };
        String result = template.fmtNxM(null, 0);
        assertEquals(ITemplate.PLACEHOLDER_NUMBER_EMPTY, result);
    }

    @Test
    void fmtNxM_shouldFormatZeroWithDecimals() {
        ITemplate template = new ITemplate() {
        };
        assertEquals("0", template.fmtNxM(0.0, 0));
        assertEquals("1,0", template.fmtNxM(1.0, 1));
        assertEquals("2,00", template.fmtNxM(2.0, 2));
    }

    @Test
    void fmtNxM_shouldFormatNegativeNumbersCorrectly() {
        ITemplate template = new ITemplate() {
        };
        assertEquals("-12 345", template.fmtNxM(-12345.0, 0));
        assertEquals("-12 345,6", template.fmtNxM(-12345.6, 1));
        assertEquals("-12 345,67", template.fmtNxM(-12345.67, 2));
    }

    @Test
    void fmtNxM_shouldHandleVerySmallNumbers() {
        ITemplate template = new ITemplate() {
        };
        assertEquals("0,0001", template.fmtNxM(0.0001, 4));
        assertEquals("0,00", template.fmtNxM(0.0001, 2));
        assertEquals("0", template.fmtNxM(0.0001, 0));
    }

    @Test
    void fmtNxM_shouldReturnNumberCorrectly() {
        ITemplate template = new ITemplate() {
        };
        String result = template.fmtNxM(12345.6789, 2);
        assertEquals("12 345,68", result);
    }

    @Test
    void fmtNxMOpc_shouldReturnRoundedNumberOnNegativeDecimals() {
        ITemplate template = new ITemplate() {
        };
        String result = template.fmtNxMOpc(12345.6789, -20);
        assertEquals("12 346", result);
    }

    @Test
    void fmtNxMOpc_shouldReturnNumberCorrectly() {
        ITemplate template = new ITemplate() {
        };
        String result = template.fmtNxMOpc(12345.6789, 2);
        assertEquals("12 345,68", result);
    }

    @Test
    void fmtNxMOpc_shouldHandleNullInput() {
        ITemplate template = new ITemplate() {
        };
        assertEquals(ITemplate.PLACEHOLDER_NUMBER_EMPTY, template.fmtNxMOpc(null, 2));
    }

    @Test
    void fmtNxMOpc_shouldFormatPositiveNumberWithZeroDecimals() {
        ITemplate template = new ITemplate() {
        };
        assertEquals("1 234", template.fmtNxMOpc(1234.0, 0));
    }

    @Test
    void fmtNxMOpc_shouldFormatPositiveNumberWithMultipleDecimals() {
        ITemplate template = new ITemplate() {
        };
        assertEquals("1 234,567", template.fmtNxMOpc(1234.567, 3));
    }

    @Test
    void fmtNxMOpc_shouldRoundValueCorrectly() {
        ITemplate template = new ITemplate() {
        };
        assertEquals("12 346", template.fmtNxMOpc(12345.6789, -20));
        assertEquals("1 234", template.fmtNxMOpc(1234.49, 0));
    }

    @Test
    void fmtNxMOpc_shouldFormatLargeNumbersConsistently() {
        ITemplate template = new ITemplate() {
        };
        assertEquals("12 345 678 901 234,56", template.fmtNxMOpc(12345678901234.56, 2));
    }

    @Test
    void fmtNxMOpc_shouldFormatNumbersAsStringConsistently() {
        ITemplate template = new ITemplate() {
        };
        assertEquals("123 456", template.fmtNxMOpc("123456"));
    }
}
