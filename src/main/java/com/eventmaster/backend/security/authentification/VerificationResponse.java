package com.eventmaster.backend.security.authentification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
public class VerificationResponse {
    @JsonProperty("message")
    private String message;

}