package com.eventmaster.backend.ControllerTests;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class CompleteTest {
    @Autowired
    private MockMvc mockMvc;


    @Nested
    @Order(1)
    class SystemAdminControllerTestOrdered {

        SystemAdminControllerTest systemAdminControllerTest = new SystemAdminControllerTest(mockMvc);
        @Test
        public void testOrga() throws Exception {
            systemAdminControllerTest.testOrganisationManagement();
        }
    }

    @Nested
    @Order(2)
    class AdminControllerTestOrdered {
        AdminControllerTest adminControllerTest = new AdminControllerTest(mockMvc);

        @Order(0)
        @Test
        public void testCreateGroup() throws Exception {
            adminControllerTest.testCreateGroup();
        }
        @Order(1)
        @Test
        public void testChangeGroup() throws Exception {
            adminControllerTest.testChangeGroup();
        }
        @Order(2)
        @Test
        public void testAddUserToGroup() throws Exception {
            adminControllerTest.testAddUserToGroup();
        }
        @Order(3)
        @Test
        public void testRemoveUserFromGroup() throws Exception {
            adminControllerTest.testRemoveUserFromGroup();
        }
    }

    @Nested
    @Order(3)
    class OrganizerControllerTestOrdered {
        OrganizerControllerTest organizerControllerTest = new OrganizerControllerTest(mockMvc);

        @Test
        public void testCreateEvent() throws Exception {
            organizerControllerTest.testCreateEvent();
        }

        @Test
        public void testChangeEvent() throws Exception {
            organizerControllerTest.testChangeEvent();
        }
    }

    @Nested
    @Order(4)
    class TutorControllerTestOrdered {
        TutorControllerTest tutorControllerTest = new TutorControllerTest(mockMvc);

        @Test
        public void getAttendeesForEvent() throws Exception {
            tutorControllerTest.getAttendeesForEvent();
        }
    }
}
