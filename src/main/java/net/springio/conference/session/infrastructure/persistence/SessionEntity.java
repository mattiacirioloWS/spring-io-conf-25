package net.springio.conference.session.infrastructure.persistence;

import jakarta.persistence.*;
import net.springio.conference.session.domain.SessionId;
import net.springio.conference.session.domain.Speakers;
import net.springio.conference.session.domain.Title;
import net.springio.conference.shared.domain.Price;
import net.springio.conference.shared.infrastructure.EntityWithEvents;

@Entity
@Table(name = "sessions")
public class SessionEntity extends EntityWithEvents<SessionEntity> {
    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id"))
    private SessionId id;

    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "title"))
    private Title title;

    @Embedded
    @AttributeOverride(name = "names", column = @Column(name = "speakers"))
    private Speakers speakers;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Price price;

    public SessionId getId() {
        return id;
    }

    public void setId(SessionId id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Speakers getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Speakers speakers) {
        this.speakers = speakers;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
