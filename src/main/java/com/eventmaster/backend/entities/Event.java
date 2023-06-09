package com.eventmaster.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as an entity to save an event in the database.
 *
 * @author Lars Holweger
 * @author Fabian Eilber
 * @author Fabian Unger
 */
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "organisationId",referencedColumnName = "id")
    private Organisation organisation;

    @ManyToOne
    @JoinColumn(name = "eventSeriesId",referencedColumnName = "id")
    private EventSeries eventSeries;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Chat> chats = new HashSet<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Document> documents = new HashSet<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Question> questions = new HashSet<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInEventWithRole> eventUserRoles = new HashSet<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<GroupInEvent> eventGroups = new HashSet<>();

    //---------------------------------------------------------------------------
    String name;
    String type;
    @Enumerated(EnumType.ORDINAL)
    EnumEventStatus status;
    boolean isPublic;
    @Column(length = 1000)
    String description;
    String image;
    String location;
    Date startDate;
    Date endDate;
    String startTime;
    String endTime;
    //---------------------------------------------------------------------------


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organization) {
        this.organisation = organization;
    }

    public EventSeries getEventSeries() {
        return eventSeries;
    }

    public void setEventSeries(EventSeries eventSeries) {
        this.eventSeries = eventSeries;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public void setChats(Set<Chat> chats) {
        this.chats = chats;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void removeDocument(Document doc) {
        documents.remove(doc);
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<UserInEventWithRole> getEventUserRoles() {
        return eventUserRoles;
    }

    public void setEventUserRoles(Set<UserInEventWithRole> eventUserRoles) {
        this.eventUserRoles = eventUserRoles;
    }

    public Set<GroupInEvent> getEventGroups() {
        return eventGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EnumEventStatus getStatus() {
        return status;
    }

    public void setStatus(EnumEventStatus status) {
        this.status = status;
    }

    public boolean getIsPublic() {
        return this.isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
