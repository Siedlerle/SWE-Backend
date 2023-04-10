package com.eventmaster.backend.UserManagement;

import com.eventmaster.backend.RoleManagement.UserEventRole.EventUserRole;
import com.eventmaster.backend.RoleManagement.UserOrgaRole.OrgaUserRole;
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
    String emailAdress;
    String password;

    boolean verified;
    //@TODO Kann jeder User eine Adresse angeben(damit man vllt als Bonus Feature events in Seiner Umgebung anzeigen kann?, PLZ abhängig/Bundesland, oder sprengt des den Rahmen?)
}
