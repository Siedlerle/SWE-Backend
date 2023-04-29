package com.eventmaster.backend.security.authorization.helper;

import com.eventmaster.backend.entities.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class EventReader {
    public static Event getEventIdFromEventObject(byte[] requestBodyInBytes){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new String(requestBodyInBytes, StandardCharsets.UTF_8), Event.class);
        } catch (IOException e) {
            return null;
        }
    }
}
