package net.videki.templateutils.template.core.provider.persistence;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Pageable query parameters
 */
@Data
public class Pageable   {
  /**
   * The page to be returned.
   * minimum: 0
   * maximum: 32768
  */
  private Integer page;

  /**
   * The number of items to be returned.
   * minimum: 0
   * maximum: 32768
  */
  private Integer size;

  /**
   * The offset to be taken.
   * minimum: 0
   * maximum: 32768
  */
  private Integer offset;

  /**
   * Returns whether the current Pageable contains pagination information.
  */
  private Boolean paged;

  /**
   * Returns whether the current Pageable does not contain pagination information.
  */
  private Boolean unpaged;

  /**
   * Sort orders in the format fieldname,asc|desc. You may define multiple sort criteria.
  */
  private List<String> sort = new LinkedList<>();

}

