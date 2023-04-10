package com.eventmaster.backend.RoleManagement.UserEventRole;

import com.eventmaster.backend.EventManagement.Event;
import com.eventmaster.backend.RoleManagement.Role.Role;
import com.eventmaster.backend.UserManagement.User;
import jakarta.persistence.*;

@Entity
public class EventUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "roleId",referencedColumnName = "id")
    private Role role;
    //---------------------------------------------------------------------------
}
