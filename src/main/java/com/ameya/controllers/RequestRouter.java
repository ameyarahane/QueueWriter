package com.ameya.controllers;

import com.ameya.guice.Injectors;
import com.ameya.messages.publisher.MessagePublisher;
import com.ameya.models.message.ApiDirectory;
import com.ameya.models.message.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@RestController
@Log4j2
public class RequestRouter {

    private final Injector injector;
    private final ObjectMapper mapper;
    private final ApiDirectory directory;
    private final MessagePublisher publisher;

    public RequestRouter() {
        injector = Injectors.createDefaultInjector();
        mapper = injector.getInstance(ObjectMapper.class);
        directory = injector.getInstance(ApiDirectory.class);
        publisher = injector.getInstance(Key.get(MessagePublisher.class, Names.named("SqsMessagePublisher")));
    }

    @GetMapping(value = "/", consumes = "application/json", produces = "application/json")
    public String listApis() {
        JsonNode apiList = directory.listApis();
        try {
            return mapper.writeValueAsString(apiList);
        } catch (JsonProcessingException e) {
            log.error("Error trying to list APIs", e);
            throw new RuntimeException("Encountered internal error retrieving list of APIs");
        }
    }

    @GetMapping(value = "/api/describe", consumes = "application/json", produces = "application/json")
    public String describeApi(@RequestParam final String apiName) {
        Objects.requireNonNull(apiName, "Parameter apiName must be non-null and non-empty");

        JsonNode apiDescription = directory.describeApi(apiName);
        try {
            return mapper.writeValueAsString(apiDescription);
        } catch (JsonProcessingException e) {
            log.error("Error fetching description for API {}", apiName, e);
            throw new RuntimeException("Could not fetch details about the API " + apiDescription);
        }
    }

    @PostMapping(value = "/api/send-message", consumes = "application/json")
    public String sendMessage(@RequestBody final Message message) throws JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        log.info("[requestId:{}] START: Processing message with id=" + message.getId());
        boolean isSuccess = false;
        try {
            validate(message, requestId);
            publisher.publish(message);
            isSuccess = true;
        } finally {
            log.info("[requestId:{}] END: Message processed {} for id={}", requestId,
                    isSuccess ? "successfully" : "unsuccessfully", message.getId());
        }

        return requestId;
    }

    private void validate(final Message message, final String requestId) {
        Objects.requireNonNull(message, "Message must be non-null");

        String exceptionMessage = null;
        if (StringUtils.isEmpty(message.getId())) {
            exceptionMessage = "Message ID field must be non-null and non-empty";
        } else if (StringUtils.isEmpty(message.getBody())) {
            exceptionMessage = "Message body field must be non-null and non-empty";
        }

        if (exceptionMessage != null) {
            throw new IllegalArgumentException(String.format("[requestId:%s] %s", requestId, exceptionMessage));
        }
    }

}
