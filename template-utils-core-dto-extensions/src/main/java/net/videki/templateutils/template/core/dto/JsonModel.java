package net.videki.templateutils.template.core.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface JsonModel {

  default String toJson() {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(this);
  }
}
