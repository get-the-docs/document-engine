package net.videki.templateutils.template.xlsx.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateData {
  private Map<String, String> data;

  public TemplateData() {
    this.data = new HashMap<>();
  }

  public Map<String, String> getData() {
    return data;
  }

  public List<String> getDetails() {
    List<String> result = new ArrayList<>(this.data.size());
    result.addAll(this.data.values());

    return result;

  }

}
