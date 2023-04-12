package com.eventmaster.backend.UserManagement;

import com.eventmaster.backend.RoleManagement.EventUserRole.EventUserRole;
import com.eventmaster.backend.RoleManagement.OrgaUserRole.OrgaUserRole;
import com.eventmaster.backend.SurveyManagement.QuestionUser.QuestionUser;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<EventUserRole> userEventRoles = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrgaUserRole> orgaUserRoles = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<QuestionUser> questionUsers = new HashSet<>();
    //---------------------------------------------------------------------------
    String firstname;
    String lastname;
    String emailAddress;
    String password;
    boolean verified;
    //@TODO Kann jeder User eine Adresse angeben(damit man vllt als Bonus Feature events in Seiner Umgebung anzeigen kann?, PLZ abhängig/Bundesland, oder sprengt des den Rahmen?)


    //---------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<EventUserRole> getUserEventRoles() {
        return userEventRoles;
    }

    public void setUserEventRoles(Set<EventUserRole> userEventRoles) {
        this.userEventRoles = userEventRoles;
    }

    public Set<OrgaUserRole> getOrgaUserRoles() {
        return orgaUserRoles;
    }

    public void setOrgaUserRoles(Set<OrgaUserRole> orgaUserRoles) {
        this.orgaUserRoles = orgaUserRoles;
    }

    public Set<QuestionUser> getQuestionUsers() {
        return questionUsers;
    }

    public void setQuestionUsers(Set<QuestionUser> questionUsers) {
        this.questionUsers = questionUsers;
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
        return emailAddress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAddress = emailAdress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
