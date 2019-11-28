package com.ameya.queuewriter.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListApisRequest extends AbstractRequest {
    private String requestId;
}
