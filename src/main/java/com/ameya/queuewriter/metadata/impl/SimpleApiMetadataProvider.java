package com.ameya.queuewriter.metadata.impl;

import com.ameya.queuewriter.metadata.ApiMetadataProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides some hand-crafted responses for the metadata APIs
 */
public class SimpleApiMetadataProvider implements ApiMetadataProvider {

    private static final String SEND_MESSAGE_API_NAME = "send-message";
    private static final String DESCRIBE_API_API_NAME = "describe-api";
    private static final String LIST_APIS_API_NAME = "list-apis";

    private final Map<String, JsonNode> apiDescriptions;
    private final ObjectMapper mapper;

    public SimpleApiMetadataProvider(final ObjectMapper mapper) {
        this.mapper = mapper;
        this.apiDescriptions = getApiDescriptions();
    }

    @Override
    public JsonNode listSupportedApis() {
        ArrayNode apis = mapper.createArrayNode();
        apiDescriptions.keySet().forEach(apis::add);

        ObjectNode data = mapper.createObjectNode();
        data.set("apis", apis);
        return data;
    }

    @Override
    public JsonNode describeApi(final String apiName) {
        return apiDescriptions.getOrDefault(apiName, NullNode.getInstance());
    }

    private Map<String, JsonNode> getApiDescriptions() {
        Map<String, JsonNode> apiMap = new HashMap();
        apiMap.put(SEND_MESSAGE_API_NAME, getSendMessageApiMetadata());
        apiMap.put(LIST_APIS_API_NAME, getListApisMetadata());
        apiMap.put(DESCRIBE_API_API_NAME, getDescribeApiMetadata());
        return Collections.unmodifiableMap(apiMap);
    }

    private JsonNode getListApisMetadata() {
        ObjectNode apiDescription = mapper.createObjectNode();
        apiDescription.set("label", new TextNode("ListApis"));
        apiDescription.set("httpMethod", new TextNode(HttpMethod.GET.name()));
        apiDescription.set("path", new TextNode("/"));
        apiDescription.set("acceptedParams", new TextNode("no params"));
        return apiDescription;
    }

    private JsonNode getDescribeApiMetadata() {
        ObjectNode apiDescription = mapper.createObjectNode();
        apiDescription.set("label", new TextNode("DescribeApi"));
        apiDescription.set("httpMethod", new TextNode(HttpMethod.GET.name()));
        apiDescription.set("path", new TextNode("/api/describe-api"));
        apiDescription.set("acceptedParams", new TextNode("apiName: String"));
        return apiDescription;
    }

    private JsonNode getSendMessageApiMetadata() {
        ObjectNode apiDescription = mapper.createObjectNode();
        apiDescription.set("label", new TextNode("SendMessage"));
        apiDescription.set("httpMethod", new TextNode(HttpMethod.POST.name()));
        apiDescription.set("path", new TextNode("/api/send-message"));
        apiDescription.set("acceptedParams", new TextNode("message: Message"));

        ObjectNode messageSchema = mapper.createObjectNode();
        messageSchema.set("body", new TextNode("The message body to be published."));
        messageSchema.set("attributes", new TextNode(
                "Any attributes that describe the message being sent, such as  size, content-type, other metadata."));
        messageSchema.set("publisherId", new TextNode("The ID of the publisher of the message."));
        apiDescription.set("messageFormat", messageSchema);

        return apiDescription;
    }
}
