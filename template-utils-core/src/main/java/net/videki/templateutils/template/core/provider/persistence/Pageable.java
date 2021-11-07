package net.videki.templateutils.template.core.provider.persistence;

import java.util.LinkedList;
import java.util.List;

/**
 * Pageable query parameters to be compatible with spring data's paging and sorting repository feature.
 * 
 * @author Levente Ban
 */
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
   * Sort orders in the format fieldname,asc|desc. You may define multiple sort criteria.
  */
  private List<String> sort = new LinkedList<>();

  /**
   * Returns the page to be returned.
   * minimum: 0
   * maximum: 32768
   * @return the page to be returned.
   */
  public Integer getPage() {
    return this.page;
  }

  /**
   * Sets the page to be returned.
   * minimum: 0
   * maximum: 32768
   * @param page the page to be returned.
   */
  public void setPage(Integer page) {
    this.page = page;
  }

  /**
   * Returns the number of items to be returned.
   * minimum: 0
   * maximum: 32768
   * @return the number of items to be returned.
   */
  public Integer getSize() {
    return this.size;
  }

  /**
   * Sets the number of items to be returned.
   * minimum: 0
   * maximum: 32768
   * @param size the number of items to be returned.
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Returns the offset to be taken.
   * minimum: 0
   * maximum: 32768
   * @return the offset to be taken.
   */
  public Integer getOffset() {
    return this.offset;
  }

  /**
   * Sets the offset to be taken.
   * minimum: 0
   * maximum: 32768
   * @param offset the offset to be taken.
   */
  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  /**
   * Returns whether the current Pageable contains pagination information.
   * @return true if paged.
   */
  public Boolean isPaged() {
    return this.paged;
  }

  /**
   * Returns whether the current Pageable contains pagination information.
   * @return true if paged.
   */
  public Boolean getPaged() {
    return this.paged;
  }

  /**
   * Sets whether the current Pageable contains pagination information.
   * @param paged true if paged.
   */
  public void setPaged(Boolean paged) {
    this.paged = paged;
  }

  /**
   * Returns whether the current Pageable does not contain pagination information.
   * @return true if unpaged.
   */
  public Boolean isUnpaged() {
    return !this.getPaged();
  }

  /**
   * Returns whether the current Pageable does not contain pagination information.
   * @return true if unpaged.
   */
  public Boolean getUnpaged() {
    return !this.getPaged();
  }

  /**
   * Sort orders in the format fieldname,asc|desc. You may define multiple sort criteria.
   * @return sort orders.
   */
  public List<String> getSort() {
    return this.sort;
  }

  /**
   * Sets sort orders in the format fieldname,asc|desc. You may define multiple sort criteria.
   * @param sort sort orders to be stored.
   */
  public void setSort(final List<String> sort) {
    this.sort = sort;
  }


  @Override
  public String toString() {
    return "{" +
      " page='" + getPage() + "'" +
      ", size='" + getSize() + "'" +
      ", offset='" + getOffset() + "'" +
      ", paged='" + isPaged() + "'" +
      ", sort='" + getSort() + "'" +
      "}";
  }

}

