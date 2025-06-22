package org.getthedocs.documentengine.core.provider.persistence;

/*-
 * #%L
 * docs-core
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

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PageableTest {

  @Test
  public void testPropertiesOk() {

    final var p = new Pageable();

    p.setOffset(8);
    p.setPage(2);
    p.setPaged(true);

    Assert.assertTrue(p.isPaged() && !p.isUnpaged() && p.getPage() == 2 && p.getOffset() == 8);

  }

    @Test
    public void testToStringMethod() {
        final var p = new Pageable();
        p.setPage(1);
        p.setSize(20);
        p.setOffset(0);
        p.setPaged(true);
        p.setSort(List.of("name,asc", "date,desc"));

        final String expected = "{ page='1', size='20', offset='0', paged='true', sort='[name,asc, date,desc]'}";

        Assert.assertEquals(expected, p.toString());
    }
}
