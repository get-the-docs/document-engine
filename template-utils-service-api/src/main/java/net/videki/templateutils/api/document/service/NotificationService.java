package net.videki.templateutils.api.document.service;

/**
 * Notification service to notify the requestor of document generation based
 * events.
 * 
 * @author Levente Ban
 */
public interface NotificationService {

  /**
   * Notifies the requestor that the request's state identified by the
   * transactionId has changed.
   * 
   * @param requestor          the requestor party to be notified (webhook url,
   *                           topic, etc.).
   * @param transactionId      the transaction id.
   * @param resultDocumentName the result document name in case of single document
   *                           generation.
   * @param successFlag        true, if the generation was successful.
   * @return true, if the notification was successful.
   */
  boolean notifyRequestor(String requestor, String transactionId, String resultDocumentName, boolean successFlag);

}
