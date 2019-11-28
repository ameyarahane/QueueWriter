package com.ameya.models.message;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class Message {

    private String id;
    private String body;
    private Map<String, String> attributes;
    private String publisher;
}
