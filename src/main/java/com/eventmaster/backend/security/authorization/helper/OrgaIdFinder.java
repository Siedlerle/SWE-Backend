package com.eventmaster.backend.security.authorization.helper;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.Group;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class OrgaIdFinder {
    //Returns the orgaId, if it was in the request,otherwise returns -1
    public static long getOrgaIdFromPath(String path) {
        String[] pathElements = path.split("/");
        ArrayList<String> pathE = new ArrayList<>(Arrays.asList(pathElements));
        long index = pathE.indexOf("orga") + 1;
        if (index <= 0) return -1;
        return Long.parseLong(pathElements[(int) index]);
    }
    public static long getOrgaIdFromEventObject(byte[] requestBodyInBytes){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Event event  = mapper.readValue(new String(requestBodyInBytes, StandardCharsets.UTF_8), Event.class);
            return event.getOrganisation().getId();
        } catch (IOException e) {
            return -1;
        }
    }
    public static long getOrgaIdFromGroupObject(byte[] requestBodyInBytes){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Group group  = mapper.readValue(new String(requestBodyInBytes, StandardCharsets.UTF_8), Group.class);
            return group.getOrganisation().getId();
        } catch (IOException e) {
            return -1;
        }
        /*
        String requestBody = new String(requestBodyInBytes, StandardCharsets.UTF_8);
        String[] requestParts = requestBody.split("\r\n");
        for (int i = 0; i < requestParts.length; i++) {
            if(requestParts[i].matches(".*organisation_id.*")){
                String organisation_id = requestParts[i].split(":")[1];
                if(organisation_id.endsWith(",")){
                    organisation_id = organisation_id.substring(0,organisation_id.length()-1);
                }
                organisation_id = organisation_id.replace(" ","");
                return Long.parseLong(organisation_id);
            };
        }
        return -1;
        */
    }
}
