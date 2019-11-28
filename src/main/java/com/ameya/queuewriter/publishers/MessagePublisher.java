package com.ameya.queuewriter.publishers;

import com.ameya.queuewriter.models.message.Message;

/**
 * Interface for publishing instances of Message.
 */
public interface MessagePublisher {

    void publish(Message message);
}
