package com.ameya.messages.publisher;

import com.ameya.models.message.Message;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogMessagePublisher implements MessagePublisher {

    @Override
    public void publish(final Message message) {
        log.info("PUBLISH MESSAGE: " + message);
    }
}
