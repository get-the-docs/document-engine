package net.videki.documentengine.core.provider.persistence;

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

import java.util.LinkedList;
import java.util.List;

/**
 * A page is a sublist of a list of objects to be compatible with spring data's paging and sorting repository feature. 
 * It allows gain information about the position of it in the containing entire list.
 * 
 * @author Levente Ban
 */
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
   * Returns the maximum number of the current Slice. Is always non-negative.
   * minimum: 0
   * maximum: 32768
  */
  private Integer size;

  /**
   * The page contents.
   */
  private List<T> data = new LinkedList<>();

  /**
   * Returns the number of total pages.
   * minimum: 0
   * maximum: 32768
   * @return the number of total pages.
   */
  public Integer getTotalPages() {
    return this.totalPages;
  }

  /**
   * Sets the number of total pages.
   * @param totalPages the number of total pages.
   */
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  /**
   * Returns the total amount of elements.
   * minimum: 0
   * maximum: 2147483647
   * @return the total amount of elements.
   */
  public Long getTotalElements() {
    return this.totalElements;
  }

  /**
   * Sets the total amount of elements.
   * minimum: 0
   * maximum: 2147483647
   * @param totalElements the total amount of elements
   */
  public void setTotalElements(Long totalElements) {
    this.totalElements = totalElements;
  }

  /**
   * Returns the number of the current Slice. Is always non-negative.
   * @return the current page number.
   */
  public Integer getNumber() {
    return this.number;
  }

  /**
   * Sets the maximum number of the current Slice. Is always non-negative.
   * @param size the number of the current Slice.
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Returns the maximum number of the current Slice. Is always non-negative.
   * @return the current page number.
   */
  public Integer getSize() {
    return this.size;
  }

  /**
   * Sets the number of the current Slice. Is always non-negative.
   * @param number the number of the current Slice.
   */
  public void setNumber(Integer number) {
    this.number = number;
  }

  /**
   * Returns the number of elements currently on this Slice.
   * minimum: 0
   * maximum: 32768
   * @return the number of elements currently on this Slice.
   */
  public Integer getNumberOfElements() {
    return this.data != null ? this.data.size() : 0;
  }

  /**
   * Returns the current page contents.
   * @return list with the page contents.
   */
  public List<T> getData() {
    return this.data;
  }

  /**
   * Sets the current page contents.
   * @param data the content list. 
   */
  public void setData(final List<T> data) {
    this.data = data;
  }

}

