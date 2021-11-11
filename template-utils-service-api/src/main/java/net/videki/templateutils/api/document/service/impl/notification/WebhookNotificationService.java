package net.videki.templateutils.api.document.service.impl.notification;

/*-
 * #%L
 * template-utils-service-api
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

import javax.annotation.PostConstruct;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.videki.templateutils.api.document.model.notification.NotificationRequest;
import net.videki.templateutils.api.document.service.NotificationService;

@RequiredArgsConstructor
@Slf4j
@Service
public class WebhookNotificationService implements NotificationService {

  private final RestTemplateBuilder restTemplateBuilder;
  private RestTemplate restTemplate;

  @PostConstruct
  protected void init() {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Override
  public boolean notifyRequestor(final String requestor, final String transactionId, final String resultDocumentName,
      final boolean successFlag) {

    if (log.isDebugEnabled()) {
      log.debug("notifyRequestor - requestor:[{}], transaction id: [{}]", requestor, transactionId);
    }

    if (requestor != null && !requestor.isBlank()) {
      final NotificationRequest notificationRequest = new NotificationRequest();
      notificationRequest.setTransactionId(transactionId);
      notificationRequest.setResultDocumentName(resultDocumentName);
      notificationRequest.setSuccessFlag(successFlag);

      final HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      final HttpEntity<NotificationRequest> request = new HttpEntity<>(notificationRequest, headers);
      final ResponseEntity<String> entity = restTemplate.exchange(requestor, HttpMethod.POST, request, String.class);

      final boolean result = entity.getStatusCode().is2xxSuccessful();

      if (!result) {
        if (resultDocumentName != null) {
          log.warn(
              "notifyRequestor - Notification NOT sent to requestor:[{}] with transaction id: [{}], result document name: [{}].",
              requestor, transactionId, resultDocumentName);
        } else {
          log.warn("notifyRequestor - Notification NOT sent to requestor:[{}] with transaction id: [{}].", requestor,
              transactionId);
        }
      }

      if (log.isDebugEnabled()) {
        log.debug("notifyRequestor end - requestor:[{}], transaction id: [{}], notification success: {}", requestor,
            transactionId, result);
      }
      return result;

    } else {
      log.debug("notifyRequestor end - requestor:[{}], transaction id: [{}], no notification was set, returning.",
          requestor, transactionId);
      return true;
    }
  }
}
