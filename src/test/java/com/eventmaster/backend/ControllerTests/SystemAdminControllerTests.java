package com.eventmaster.backend.ControllerTests;

import com.eventmaster.backend.entities.Organisation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
public class SystemAdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateOrganisation() throws Exception {
        Organisation organisation = new Organisation();
        organisation.setName("TestOrgaName");
        organisation.setLocation("TestOrgaLocation");
        String sysAdminPassword = "password";

        mockMvc.perform(post("/sys-admin/organisation/create")
                        .content(asJsonString(organisation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sysAdminPassword", sysAdminPassword))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Organisation created successfully"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
