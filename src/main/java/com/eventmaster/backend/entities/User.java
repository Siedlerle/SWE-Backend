package com.eventmaster.backend.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as an entity to save a user in the database.
 *
 * @author Fabian Eilber
 * @author Lars Holweger
 * @author Fabian Unger
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInEventWithRole> userInEventWithRoles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<QuestionAnsweredByUser> questionAnsweredByUserSet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInGroup> userInGroupSet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInOrgaWithRole> userInOrgaWithRoleSet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Chat> chats = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    //---------------------------------------------------------------------------

    private String firstname;
    private String lastname;
    private String emailAdress;
    private String password;

    //---------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<UserInEventWithRole> getUserInEventWithRoles() {
        return userInEventWithRoles;
    }

    public void setUserInEventWithRoles(Set<UserInEventWithRole> userInEventWithRoles) {
        this.userInEventWithRoles = userInEventWithRoles;
    }

    public Set<QuestionAnsweredByUser> getQuestionAnsweredByUserSet() {
        return questionAnsweredByUserSet;
    }

    public void setQuestionAnsweredByUserSet(Set<QuestionAnsweredByUser> questionAnsweredByUserSet) {
        this.questionAnsweredByUserSet = questionAnsweredByUserSet;
    }

    public Set<UserInGroup> getUserInGroupSet() {
        return userInGroupSet;
    }

    public void setUserInGroupSet(Set<UserInGroup> userInGroupSet) {
        this.userInGroupSet = userInGroupSet;
    }

    public Set<UserInOrgaWithRole> getUserInOrgaWithRoleSet() {
        return userInOrgaWithRoleSet;
    }

    public void setUserInOrgaWithRoleSet(Set<UserInOrgaWithRole> userInOrgaWithRoleSet) {
        this.userInOrgaWithRoleSet = userInOrgaWithRoleSet;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public void setChats(Set<Chat> chats) {
        this.chats = chats;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
