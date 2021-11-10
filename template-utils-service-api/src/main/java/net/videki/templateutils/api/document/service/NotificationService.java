/*
 * Copyright (c) 2021. Levente Ban
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
