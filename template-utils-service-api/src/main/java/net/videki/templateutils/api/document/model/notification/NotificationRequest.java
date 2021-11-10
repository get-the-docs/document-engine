package net.videki.templateutils.api.document.model.notification;

import lombok.Data;

/**
 * Notification request.
 * @author Levente Ban
 */
@Data
public class NotificationRequest   {
  private String documentStructureId;
  private String transactionId;
  private String resultDocumentName;
  private boolean successFlag;
}

