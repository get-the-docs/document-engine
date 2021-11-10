/*
 * Copyright (c) 2021. Levente Ban
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.videki.templateutils.template.core.provider.persistence;

import org.junit.Assert;
import org.junit.Test;

public class PageableTest {

  @Test
  public void testPropertiesOk() {

    final var p = new Pageable();

    p.setOffset(8);
    p.setPage(2);
    p.setPaged(true);

    Assert.assertTrue(p.isPaged() && !p.isUnpaged() && p.getPage() == 2 && p.getOffset() == 8);

  }

}
