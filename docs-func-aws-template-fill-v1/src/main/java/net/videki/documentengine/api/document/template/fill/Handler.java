package net.videki.documentengine.api.document.template.fill;

/*-
 * #%L
 * docs-func-aws-template-fill-v1
 * %%
 * Copyright (C) 2021 - 2023 Levente Ban
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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import lombok.extern.slf4j.Slf4j;
import net.videki.documentengine.core.context.JsonTemplateContext;
import net.videki.documentengine.core.service.TemplateServiceRegistry;
import net.videki.documentengine.core.service.exception.TemplateServiceException;
import net.videki.documentengine.core.service.exception.TemplateServiceRuntimeException;


@Slf4j
public class Handler implements RequestHandler<SQSEvent, Void> {
    public static final String REQ_TEMPLATE_ID = "templateId";
    public static final String REQ_NOTIFICATION_URL = "notificationUrl";
    @Override
    public Void handleRequest(final SQSEvent event, final Context context)
    {
        for(SQSMessage msg : event.getRecords()) {
            final String messageId = msg.getMessageId();

            try {
                log.info("Filling document for message id: [{}]", messageId);

                final SQSEvent.MessageAttribute templateIdAttribute = msg.getMessageAttributes().get(REQ_TEMPLATE_ID);
                final SQSEvent.MessageAttribute notificationUrlAttribute = msg.getMessageAttributes().get(REQ_NOTIFICATION_URL);

                final String templateId = templateIdAttribute != null ? templateIdAttribute.getStringValue() : null;
                final String notificationUrl = notificationUrlAttribute != null ? notificationUrlAttribute.getStringValue() : null;

                if (templateId == null) {
                    log.warn("No template id provided for message: [{}]. The message will be discarded.", messageId);

                    return null;
                }
                log.debug("Filling document for message id: [{}], template id: [{}]", messageId, templateId);

                fill(messageId, templateId, msg.getBody(), notificationUrl);

                log.info("Fill complete for message id: [{}]", messageId);
                log.debug("Fill complete for message id: [{}], template id: [{}]", messageId, templateId);
            } catch (final Exception e) {
                log.error("Error processing message: [{}]", messageId);
            }
        }

        return null;
    }

    private void fill(final String transactionId, final String id, final Object body,
                      final String notificationUrl) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJobAsync - transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
            }
            if (log.isTraceEnabled()) {
                log.trace("postTemplateGenerationJobAsync - transaction id: [{}] template id: [{}], data: [{}]", transactionId, id, body);
            }

            final var genResult = TemplateServiceRegistry.getInstance().fillAndSave(transactionId, id,
                    new JsonTemplateContext((String)(body)));

//            this.notificationService.notifyRequestor(notificationUrl, genResult.getTransactionId(),
//                    genResult.getFileName(), genResult.isGenerated());

            if (log.isDebugEnabled()) {
                log.debug("postTemplateGenerationJobAsync end - transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
            }
            if (log.isTraceEnabled()) {
                log.trace("postTemplateGenerationJobAsync end - transaction id: [{}], template id: [{}], data: [{}]", transactionId, id, genResult);
            }
        } catch (final TemplateServiceException | TemplateServiceRuntimeException e) {
            log.warn("Error processing request: transaction id: [{}], template id:[{}], notification url: [{}]", transactionId, id, notificationUrl);
        }

    }

}
