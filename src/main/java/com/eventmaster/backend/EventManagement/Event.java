package com.eventmaster.backend.EventManagement;

import com.eventmaster.backend.InformationDistribution.Chat.Chat;
import com.eventmaster.backend.InformationDistribution.Document.Document;
import com.eventmaster.backend.OrganizationManagement.Organization;
import com.eventmaster.backend.RoleManagement.EventUserRole.EventUserRole;
import com.eventmaster.backend.SurveyManagement.Question.Question;
import jakarta.persistence.*;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "organizationId",referencedColumnName = "id")
    private Organization organization;

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Chat> chats = new HashSet<>();

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<EventUserRole> eventUserRoles = new HashSet<>();

    //---------------------------------------------------------------------------
    String name;
    String type;
    String description;
    File image;
    //@TODO brauchen wir hier eine Adress Tabelle oder wollen wir location als String speichern? (Falls ja, kann man auch jeder Organiazion Adresse(n) zuweisen)
    String location;
    //@TODO Hier entscheiden welches Date man importieren will, verschiedene Dates verhalten sich unterschiedlich
    //Wenn man Timestamp verwendet hat man 2 Einträge die in ms angegeben sind, oder man spaltet Zeit von Datum
    Timestamp startTime;
    Timestamp endTime;
}
