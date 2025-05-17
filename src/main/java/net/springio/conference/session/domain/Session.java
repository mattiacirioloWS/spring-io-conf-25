package net.springio.conference.session.domain;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Factory;
import org.jmolecules.ddd.annotation.Identity;

@AggregateRoot
public class Session {
    @Identity
    private final SessionId id;
    private final Title title;
    private final Speakers speakers;
    private final Price price;

    private Session(SessionId id, Title title, Speakers speakers, Price price) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (title == null || title.text().isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (speakers == null || speakers.names().isBlank()) {
            throw new IllegalArgumentException("Speakers cannot be null or blank");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        this.id = id;
        this.title = title;
        this.speakers = speakers;
        this.price = price;
    }

    public SessionId getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public Speakers getSpeakers() {
        return speakers;
    }

    public Price getPrice() {
        return price;
    }

    @Factory
    public static final class Builder {
        public static Session reconstitute(SessionId id, Title title, Speakers speakers, Price price) {
            return new Session(id, title, speakers, price);
        }
    }
}
