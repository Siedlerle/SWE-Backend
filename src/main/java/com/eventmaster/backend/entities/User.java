package com.eventmaster.backend.entities;


import com.eventmaster.backend.security.Token.Token;
import com.eventmaster.backend.serviceswithouttoken.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * This class serves as an entity to save a user in the database.
 *
 * @author Fabian Eilber
 * @author Lars Holweger
 * @author Fabian Unger
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstname;
    private String lastname;
    private String emailAdress;
    private String password;

    private boolean enabled;

    //@Enumerated(EnumType.STRING)
    //private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    //---------------------------------------------------------------------------

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
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<UserInOrgaWithRole> userInOrgaWithRoleSet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Chat> chats = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    //---------------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    public void setPassword(String password) {
        this.password = password;
    }


    //---------------------------------------------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String role;
        for(UserInOrgaWithRole uiOwR : userInOrgaWithRoleSet){
            role = "ROLE_"+uiOwR.getOrganisation().getId()+"_"+uiOwR.getOrgaRole().getRole().role;
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailAdress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled( boolean enabled){
        this.enabled = enabled;
    }


}
