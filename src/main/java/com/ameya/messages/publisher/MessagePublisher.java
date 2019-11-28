package com.ameya.messages.publisher;

import com.ameya.models.message.Message;

/**
 * Interface for publishing instances of Message.
 */
public interface MessagePublisher {

    void publish(Message message);
}
