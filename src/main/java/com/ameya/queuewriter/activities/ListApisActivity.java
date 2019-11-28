package com.ameya.queuewriter.activities;

import com.ameya.queuewriter.metadata.ApiMetadataProvider;
import com.ameya.queuewriter.models.requests.AbstractRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Lists available APIs by their names.
 */
@RequiredArgsConstructor
@Log4j2
public class ListApisActivity extends AbstractActivity<String> {

    private final ObjectMapper objectMapper;
    private final ApiMetadataProvider metadataProvider;

    /**
     * Publishes the message passed in to the publisher associated with the service.
     * <p>
     * Returns successfully if the operation was successful else throws the appropriate exception back to the client.
     */
    @Override
    public String processRequest(final AbstractRequest request) {
        JsonNode apiList = metadataProvider.listSupportedApis();
        try {
            return objectMapper.writeValueAsString(apiList);
        } catch (JsonProcessingException e) {
            log.error("Error trying to list APIs", e);
            throw new RuntimeException("Encountered internal error retrieving list of APIs");
        }
    }
}
