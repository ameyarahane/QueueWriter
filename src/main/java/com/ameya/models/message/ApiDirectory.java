package com.ameya.models.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.Map;

public class ApiDirectory {

    private static final String SEND_MESSAGE_API_PATH = "send-message";
    private final ObjectMapper mapper;
    private final Map<String, JsonNode> apiDescriptions;

    public ApiDirectory(final ObjectMapper mapper) {
        this.mapper = mapper;
        apiDescriptions = createApiDescriptions();
    }

    public JsonNode listApis() {
        ArrayNode apis = mapper.createArrayNode();
        apiDescriptions.keySet().forEach(apis::add);

        ObjectNode data = mapper.createObjectNode();
        data.set("apis", apis);
        return data;
    }

    public JsonNode describeApi(final String apiName) {
        return apiDescriptions.getOrDefault(apiName, NullNode.getInstance());
    }

    private Map<String, JsonNode> createApiDescriptions() {
        return Collections.singletonMap(SEND_MESSAGE_API_PATH, describeSendMessageApi());
    }

    private JsonNode describeSendMessageApi() {
        ObjectNode apiDescription = mapper.createObjectNode();
        apiDescription.set("label", new TextNode("SendMessage"));
        apiDescription.set("httpMethod", new TextNode(HttpMethod.POST.name()));
        apiDescription.set("path", new TextNode("/api/send-message"));

        ObjectNode messageSchema = mapper.createObjectNode();
        messageSchema.set("body", new TextNode("The message body to be published."));
        messageSchema.set("attributes", new TextNode(
                "Any attributes that describe the message being sent, such as  size, content-type, other metadata."));
        messageSchema.set("publisherId", new TextNode("The ID of the publisher of the message."));
        apiDescription.set("messageFormat", messageSchema);

        ObjectNode identity = mapper.createObjectNode();
        identity.set("clientId", new TextNode("A value that uniquely identifies the client."));
        identity.set("clientSecret", new TextNode("A secret known to the client used for AuthN/AuthZ by the service"));
        apiDescription.set("identity", identity);

        return apiDescription;
    }
}
