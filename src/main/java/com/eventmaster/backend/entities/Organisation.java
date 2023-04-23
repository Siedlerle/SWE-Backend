package com.eventmaster.backend.entities;

import jakarta.persistence.*;

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

    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInOrgaWithRole> orgaUserRoles = new HashSet<>();

    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Preset> presets = new HashSet<>();

    private String name;

    private String location;

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
}
