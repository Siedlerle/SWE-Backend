package com.eventmaster.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class MessageResponse {
    @JsonProperty("message")
    private String message;

}
