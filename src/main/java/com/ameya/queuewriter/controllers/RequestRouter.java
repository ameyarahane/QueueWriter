package com.ameya.queuewriter.controllers;

import com.ameya.queuewriter.activities.DescribeApiActivity;
import com.ameya.queuewriter.activities.ListApisActivity;
import com.ameya.queuewriter.activities.SendMessageActivity;
import com.ameya.queuewriter.guice.Injectors;
import com.ameya.queuewriter.models.message.Message;
import com.ameya.queuewriter.models.requests.DescribeApiRequest;
import com.ameya.queuewriter.models.requests.ListApisRequest;
import com.ameya.queuewriter.models.requests.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * A request router that maps different RESTful resource paths to various handlers. This can be extended by defining
 * each resource path in its own controller class if needed.
 */
@RestController
@Log4j2
public class RequestRouter {

    private final Injector injector;
    private final ObjectMapper mapper;

    public RequestRouter() {
        injector = Injectors.createDefaultInjector();
        mapper = injector.getInstance(ObjectMapper.class);
    }

    @GetMapping(value = "/api/describe-api", consumes = "application/json", produces = "application/json")
    public String describeApi(@RequestParam final String apiName) {
        DescribeApiActivity activity = injector.getInstance(DescribeApiActivity.class);
        return activity.processRequest(new DescribeApiRequest(getRequestId(), apiName));
    }

    @GetMapping(value = "/", consumes = "application/json", produces = "application/json")
    public String listApis() {
        ListApisActivity activity = injector.getInstance(ListApisActivity.class);
        String result = activity.handleRequest(new ListApisRequest(getRequestId()));
        return result;
    }

    @PostMapping(value = "/api/send-message", consumes = "application/json")
    public void sendMessage(@RequestBody final Message message) {
        SendMessageActivity activity = injector.getInstance(SendMessageActivity.class);
        SendMessageRequest request = new SendMessageRequest(message, getRequestId());
        activity.handleRequest(request);
    }

    private String getRequestId() {
        return UUID.randomUUID().toString();
    }
}
