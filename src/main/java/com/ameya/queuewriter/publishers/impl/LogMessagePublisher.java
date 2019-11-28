package com.ameya.queuewriter.publishers.impl;

import com.ameya.queuewriter.models.message.Message;
import com.ameya.queuewriter.publishers.MessagePublisher;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogMessagePublisher implements MessagePublisher {

    @Override
    public void publish(final Message message) {
        log.info("PUBLISH MESSAGE: " + message);
    }
}
