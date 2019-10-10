package net.videki.templateutils.template;

import java.util.LinkedList;
import java.util.List;

public class TableDTO {

  private List<TableRowDTO> rowdata;

  TableDTO() {
    this.rowdata = new LinkedList<>();
    this.rowdata.add(new TableRowDTO("name1", "actor1"));
    this.rowdata.add(new TableRowDTO("name2", "actor2"));
    this.rowdata.add(new TableRowDTO("name3", "actor3"));
  }

  public List<TableRowDTO> getRowdata() {
    return rowdata;
  }
  
  
}
