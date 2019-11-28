package com.ameya.queuewriter.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DescribeApiRequest extends AbstractRequest {
    private String requestId;
    private String apiName;
}
