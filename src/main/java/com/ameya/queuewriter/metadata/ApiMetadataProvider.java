package com.ameya.queuewriter.metadata;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * An interface
 */
public interface ApiMetadataProvider {

    JsonNode listSupportedApis();

    JsonNode describeApi(String apiName);
}



