package com.ameya.queuewriter.models.requests;

import com.ameya.queuewriter.models.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMessageRequest extends AbstractRequest {
    private Message message;
    private String requestId;
}
