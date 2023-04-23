package com.eventmaster.backend.ControllerTests;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInEventWithRole;
import com.eventmaster.backend.serviceswithouttoken.UserInEventWithRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.client.match.ContentRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TutorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserInEventWithRoleService userInEventWithRoleService;

    public TutorControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void shouldCreateMockMvc(){
        assertNotNull(mockMvc);
    }


    void getAttendeesForEvent() throws Exception {

        User user = new User();
        user.setFirstname("Max");
        user.setLastname("Mustermann");


        ObjectMapper objectMapper = new ObjectMapper();

        when(userInEventWithRoleService.getAttendeesForEvent(any())).thenReturn(List.of(user));

        mockMvc.perform((RequestBuilder) post("/tutor/event/{eventId}/attendees/get-all").body(12345))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("max"));

    }

    @Test
    void getAttendingStatusForUsers() {
    }

    @Test
    void updateAttendingStatusForUsers() {
    }

    @Test
    void addUserToEvent() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void uploadFile() {
    }

    @Test
    void deleteFile() {
    }

    @Test
    void addQuestion() {
    }
}