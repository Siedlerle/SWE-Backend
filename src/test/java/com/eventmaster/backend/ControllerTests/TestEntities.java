package com.eventmaster.backend.ControllerTests;


import com.eventmaster.backend.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;

@SpringBootTest
public class TestEntities {


    protected static Organisation testOrganisation = new Organisation();
    protected Event testEvent = new Event();
    protected User testOrganizer = new User();
    protected Group testGroup = new Group();

    public TestEntities() {
        initTestOrganisation();
        initTestEvent();
        initTestOrganizer();
        initTestGroup();
    }


    private void initTestOrganisation() {
        testOrganisation.setId(123);
        testOrganisation.setName("TestOrgaName");
        testOrganisation.setLocation("TestOrgaLocation");
    }

    public void setTestOrganisationId(long id) {
        System.out.println(id);
        testOrganisation.setId(id);
    }

    public void initTestEvent() {
        testEvent.setId(123);
        testEvent.setName("TestEvent");
        testEvent.setType("Workshop");
        testEvent.setStatus(EnumEventStatus.SCHEDULED);
        testEvent.setDescription("Event für den Test");
        testEvent.setLocation("Besprechungsraum");
        testEvent.setStartDate(Date.valueOf("2023-05-11"));
        testEvent.setEndDate(Date.valueOf("2023-05-14"));
        testEvent.setStartTime(new Time(11,30,0));
        testEvent.setEndTime(new Time(19,30,0));
    }

    public void initTestOrganizer() {
        testOrganizer.setId((long) 123);
        testOrganizer.setFirstname("Der");
        testOrganizer.setLastname("Organizer");
        testOrganizer.setEmailAdress("der.organizer@ldsfjbdlasbf.de");
        testOrganizer.setPassword("safePasswordFromOrganizer");
    }

    private void initTestGroup() {
        testGroup.setId(123);
        testGroup.setOrganisation(testOrganisation);
        testGroup.setName("TestGruppe");
        //testOrganisation.addGroup(testGroup);
    }
}
