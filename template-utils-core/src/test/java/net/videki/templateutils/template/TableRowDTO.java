package net.videki.templateutils.template;

public class TableRowDTO {

  private String name;
  private String actor;
  
  

  TableRowDTO(String name, String actor) {
    super();
    this.name = name;
    this.actor = actor;
  }

  public String getName() {
    return name;
  }

  public String getActor() {
    return actor;
  }

}
