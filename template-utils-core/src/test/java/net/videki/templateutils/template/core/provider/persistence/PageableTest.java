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
