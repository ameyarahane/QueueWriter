package com.ameya.queuewriter.activities;

import com.ameya.queuewriter.exceptions.ClientException;
import com.ameya.queuewriter.exceptions.InternalException;
import com.ameya.queuewriter.metadata.ApiMetadataProvider;
import com.ameya.queuewriter.models.requests.AbstractRequest;
import com.ameya.queuewriter.models.requests.DescribeApiRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

/**
 * Describes a given API request format.
 */
@RequiredArgsConstructor
@Log4j2
public class DescribeApiActivity extends AbstractActivity<String> {

    private final ObjectMapper objectMapper;
    private final ApiMetadataProvider metadataProvider;

    /**
     * Publishes the message passed in to the publisher associated with the service.
     * <p>
     * Returns successfully if the operation was successful else throws the appropriate exception back to the client.
     */
    @Override
    public String processRequest(final AbstractRequest request) {
        return handleRequest((DescribeApiRequest) request);
    }

    private String handleRequest(final DescribeApiRequest request) {
        if (request == null || StringUtils.isEmpty(request.getApiName())) {
            String usageStr = "Usage: describe-api?apiName={apiName}";
            throw new ClientException("Missing parameter API name in request. " + usageStr);
        }

        String apiName = request.getApiName();

        JsonNode apiDescription = metadataProvider.describeApi(apiName);
        try {
            return objectMapper.writeValueAsString(apiDescription);
        } catch (JsonProcessingException e) {
            log.error("Error fetching description for API {}", apiName, e);
            throw new InternalException("Could not fetch details about the API " + apiDescription);
        }
    }
}
