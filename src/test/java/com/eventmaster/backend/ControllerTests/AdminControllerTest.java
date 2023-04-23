package com.eventmaster.backend.ControllerTests;

import com.eventmaster.backend.entities.Group;
import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.entities.User;
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
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public AdminControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private ObjectMapper mapper = new ObjectMapper();

    public long testCreateGroup(Organisation testOrganisation, Group testGroup) throws Exception {
        mockMvc.perform(post("/admin/orga/" + testOrganisation.getId() + "/group/add")
                .content(asJsonString(mapper, testGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.GROUPCREATEDSUCCESSMESSAGE));
        return testGroup.getId();
    }

    public void testChangeGroup(Group testGroup) throws Exception {
        mockMvc.perform(post("/admin/group/change")
                .content(asJsonString(mapper, testGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.GROUPCHANGEDSUCCESSMESSAGE));
    }

    public void testAddUserToGroup(User testOrganizer, Group testGroup) throws Exception {
        mockMvc.perform(post("/admin/group/" + testGroup.getId() + "/user/add")
                .content(asJsonString(mapper, testOrganizer.getEmailAdress()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.ADDEDUSERTOGROUPSUCCESSMESSAGE));
    }

    public void testRemoveUserFromGroup(User testOrganizer, Group testGroup) throws Exception {
        mockMvc.perform(post("/admin/group/" + testGroup.getId() + "/user/remove")
                .content(asJsonString(mapper, testOrganizer.getEmailAdress()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.REMOVEDUSERFROMGROUPSUCCESSMESSAGE));
    }

    protected static String asJsonString(ObjectMapper mapper, final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
