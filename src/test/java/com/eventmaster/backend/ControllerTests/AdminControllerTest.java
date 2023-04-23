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
public class AdminControllerTest extends TestEntities {

    @Autowired
    private MockMvc mockMvc;

    public AdminControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private ObjectMapper mapper = new ObjectMapper();

    public void testCreateGroup() throws Exception {
        mockMvc.perform(post("/admin/orga/" + testOrganisation.getId() + "/group/add")
                .content(asJsonString(mapper, testGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.GROUPCREATEDSUCCESSMESSAGE));
    }

    public void testChangeGroup() throws Exception {
        testGroup.setName("NeuerNameFürTestGruppe");
        mockMvc.perform(post("/admin/group/change")
                .content(asJsonString(mapper, testGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.EVENTCHANGEDSUCCESSMESSAGE));
    }

    public void testAddUserToGroup() throws Exception {
        mockMvc.perform(post("/admin/group/" + testGroup.getId() + "/user/add")
                .content(asJsonString(mapper, testOrganizer.getEmailAdress()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.ADDEDUSERTOGROUPSUCCESSMESSAGE));
    }

    public void testRemoveUserFromGroup() throws Exception {
        mockMvc.perform(post("/admin/group/" + testGroup.getId() + "/user/remove")
                .content(asJsonString(mapper, testOrganizer.getEmailAdress()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.REMOVEDUSERFROMGROUPSUCCESSMESSAGE));
    }
}
