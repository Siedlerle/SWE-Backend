package com.eventmaster.backend.ControllerTests;

import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import local.variables.LocalizedStringVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
public class SystemAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();
    public SystemAdminControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public void createUser(User user) throws Exception {
        String sysAdminPassword = "password";
        mockMvc.perform(post("/sys-admin/user/add")
                        .content(asJsonString(mapper, user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sysAdminPassword", sysAdminPassword))

                .andExpect(status().isOk())
                .andExpect(content().string("saved"));
    }

    public long testOrganisationManagement(Organisation testOrganisation) throws Exception {
        String sysAdminPassword = "password";



        mockMvc.perform(post("/sys-admin/organisation/create")
                        .content(asJsonString(mapper, testOrganisation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sysAdminPassword", sysAdminPassword))
                    .andExpect(status().isOk())
                    .andExpect(content().string(LocalizedStringVariables.ORGACREATEDSUCCESSMESSAGE));


        MvcResult ra = mockMvc.perform(post("/user/orga/get-all")
                        .content(asJsonString(mapper, testOrganisation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sysAdminPassword", sysAdminPassword))
                .andExpect(status().isOk())
                .andReturn();

        String content = ra.getResponse().getContentAsString();

        List<Organisation> organisations = mapper.readValue(content, new TypeReference<List<Organisation>>(){});

        for (Organisation orga: organisations) {
            if (testOrganisation.getName().equals(orga.getName()))
            {
                testOrganisation = orga;
            }
        }


        testOrganisation.setName("NewTestOrgaName");

        mockMvc.perform(post("/sys-admin/organisation/change")
                        .content(asJsonString(mapper, testOrganisation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sysAdminPassword", sysAdminPassword))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.ORGACHANGEDSUCCESSMESSAGE));

        /*
        mockMvc.perform(post("/sys-admin/organisation/delete/" + testOrganisation.getId())
                        .content(asJsonString(mapper, testOrganisation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sysAdminPassword", sysAdminPassword))
                .andExpect(status().isOk())
                .andExpect(content().string(LocalizedStringVariables.ORGADELETEDSUCCESSMESSAGE));
         */
        return testOrganisation.getId();
    }

    protected static String asJsonString(ObjectMapper mapper, final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
