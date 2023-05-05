package com.eventmaster.backend.ControllerTests;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Time;


@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class CompleteTest {
    @Autowired
    private MockMvc mockMvc;


    protected static Organisation testOrganisation = new Organisation();
    protected Event testEvent = new Event();
    protected User testOrganizer = new User();
    protected Group testGroup = new Group();

    public CompleteTest() {
        initTestOrganisation();
        initTestEvent();
        initTestOrganizer();
        initTestGroup();
    }

    private void initTestOrganisation() {
        testOrganisation.setId((long) 123);
        testOrganisation.setName("TestOrgaName");
        testOrganisation.setLocation("TestOrgaLocation");
    }

    public void initTestEvent() {
        testEvent.setId((long) 123);
        testEvent.setName("TestEvent");
        testEvent.setType("Workshop");
        testEvent.setStatus(EnumEventStatus.SCHEDULED);
        testEvent.setDescription("Event für den Test");
        testEvent.setLocation("Besprechungsraum");
        testEvent.setStartDate(Date.valueOf("2023-05-11"));
        testEvent.setEndDate(Date.valueOf("2023-05-14"));
        testEvent.setStartTime((new Time(11,30,0)).toString());
        testEvent.setEndTime((new Time(19,30,0)).toString());
    }

    public void initTestOrganizer() {
        testOrganizer.setId((long) 123);
        testOrganizer.setFirstname("Der");
        testOrganizer.setLastname("Organizer");
        testOrganizer.setEmailAdress("der.organizer@ldsfjbdlasbf.de");
        testOrganizer.setPassword("safePasswordFromOrganizer");
        testOrganizer.setEnabled(true);
    }

    private void initTestGroup() {
        testGroup.setId((long) 123);
        testGroup.setOrganisation(testOrganisation);
        testGroup.setName("TestGruppe");
        //testOrganisation.addGroup(testGroup);
    }



    @Nested
    @Order(1)
    class SystemAdminControllerTestOrdered {

        SystemAdminControllerTest systemAdminControllerTest = new SystemAdminControllerTest(mockMvc);
        @Test
        public void testOrga() throws Exception {
            long id = systemAdminControllerTest.testOrganisationManagement(testOrganisation);
            testOrganisation.setId(id);
        }
        @Test
        public void testCreateUser() throws Exception {
            systemAdminControllerTest.createUser(testOrganizer);
        }
    }

    @Nested
    @Order(2)
    class AdminControllerTestOrdered {
        AdminControllerTest adminControllerTest = new AdminControllerTest(mockMvc);

        @Order(0)
        @Test
        public void testCreateGroup() throws Exception {
            long id = adminControllerTest.testCreateGroup(testOrganisation, testGroup);
            testGroup.setId(id);
        }
        @Order(1)
        @Test
        public void testChangeGroup() throws Exception {
            testGroup.setName("NeuerNameFürTestGruppe");
            adminControllerTest.testChangeGroup(testGroup);
        }
        @Order(2)
        @Test
        public void testAddUserToGroup() throws Exception {
            adminControllerTest.testAddUserToGroup(testOrganizer, testGroup);
        }
        @Order(3)
        @Test
        public void testRemoveUserFromGroup() throws Exception {
            adminControllerTest.testRemoveUserFromGroup(testOrganizer, testGroup);
        }
    }

    @Nested
    @Order(3)
    class OrganizerControllerTestOrdered {
        OrganizerControllerTest organizerControllerTest = new OrganizerControllerTest(mockMvc);

        @Test
        public void testCreateEvent() throws Exception {
            organizerControllerTest.testCreateEvent(testEvent, testOrganizer);
        }

        @Test
        public void testChangeEvent() throws Exception {
            testEvent.setName("NeuerNameFürTestEvent");
            organizerControllerTest.testChangeEvent(testEvent);
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
