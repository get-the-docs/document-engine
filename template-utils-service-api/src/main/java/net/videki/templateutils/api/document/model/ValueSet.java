package net.videki.templateutils.api.document.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * ValueSet.
 */

@Data
public class ValueSet   {
  private String documentStructureId;
  private String transactionId;
  private String locale;
  private List<ValueSetItem> values = new LinkedList<>();
}

