package net.videki.templateutils.template.core.provider.persistence;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

/**
 * A page is a sublist of a list of objects. It allows gain information about the position of it in the containing entire list.
 */
@Data
public class Page<T extends Object>   {
    /**
   * Returns the number of total pages.
   * minimum: 0
   * maximum: 32768
  */
  private Integer totalPages;

  /**
   * Returns the total amount of elements.
   * minimum: 0
   * maximum: 2147483647
  */
  private Long totalElements;

  /**
   * Returns the number of the current Slice. Is always non-negative.
   * minimum: 0
   * maximum: 32768
  */
  private Integer number;

  /**
   * Returns the number of elements currently on this Slice.
   * minimum: 0
   * maximum: 32768
  */
  private Integer size;

  /**
   * Returns the number of elements currently on this Slice.
   * minimum: 0
   * maximum: 32768
  */  
  private Integer numberOfElements;

  private List<T> data = new LinkedList<>();
}

