package com.eventmaster.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as an entity to save an organisation in the database.
 *
 * @author Lars Holweger
 * @author Fabian Eilber
 * @author Fabian Unger
 */
@Entity

public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Event> events = new HashSet<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInOrgaWithRole> orgaUserRoles = new HashSet<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Group> groups = new HashSet<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Preset> presets = new HashSet<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<EventSeries> eventSeries = new HashSet<>();

    private String name;

    private String location;

    String image;

    //---------------------------------------------------------------------------

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public Set<Preset> getPresets() {
        return presets;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void setOrgaUserRoles(Set<UserInOrgaWithRole> orgaUserRoles) {
        this.orgaUserRoles = orgaUserRoles;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public void setPresets(Set<Preset> presets) {
        this.presets = presets;
    }

    public void removeUserInOrgaWithRole(UserInOrgaWithRole userInOrgaWithRole) {
        this.orgaUserRoles.remove(userInOrgaWithRole);
    }
    public Set<UserInOrgaWithRole> getOrgaUserRoles() {
        return orgaUserRoles;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Set<EventSeries> getEventSeries() {
        return eventSeries;
    }

    public void setEventSeries(Set<EventSeries> eventSeries) {
        this.eventSeries = eventSeries;
    }
}
