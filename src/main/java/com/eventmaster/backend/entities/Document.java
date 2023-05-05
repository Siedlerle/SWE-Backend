package com.eventmaster.backend.entities;

import com.eventmaster.backend.entities.Event;
import jakarta.persistence.*;

/**
 * This class serves as an entity to save a document in the database.
 *
 * @author Fabian Eilber
 * @authoer Lars Holweger
 */
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name ="eventId",referencedColumnName = "id")
    private Event event;
    //---------------------------------------------------------------------------
    String name;
    String downloadUri;
    long size;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
