package com.eventmaster.backend.InformationDistribution.Chat;

import com.eventmaster.backend.EventManagement.Event;
import com.eventmaster.backend.InformationDistribution.Comment.Comment;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;

    @OneToMany(mappedBy = "chat",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    //---------------------------------------------------------------------------
    String message;

    //---------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }
}
