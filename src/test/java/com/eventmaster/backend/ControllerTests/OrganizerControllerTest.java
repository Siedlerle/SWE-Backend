package com.eventmaster.backend.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.variables.LocalizedStringVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
public class OrganizerControllerTest extends TestEntities {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    public OrganizerControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


    public void testCreateEvent() throws Exception {
        mockMvc.perform(post("/organizer/event/create")
                        .content(asJsonString(mapper, testEvent))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userMail", testOrganizer.getEmailAdress()))
                        .andExpect(status().isOk())
                        .andExpect(content().string(LocalizedStringVariables.EVENTCREATEDSUCCESSMESSAGE));
    }

    public void testChangeEvent() throws Exception {
        testEvent.setName("NeuerNameFürTestEvent");
        mockMvc.perform(post("/organizer/event/change")
                .content(asJsonString(mapper, testEvent))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.EVENTCHANGEDSUCCESSMESSAGE));
    }



    /*
    @Test
    public void testDeleteEvent() throws Exception {
        mockMvc.perform(post("/organizer/event/delete/" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.EVENTDELETEDSUCCESSMESSAGE));
    }
    */
}
