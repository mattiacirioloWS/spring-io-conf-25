package net.springio.conference.session.domain;

import net.springio.conference.session.domain.event.PriceChangedEvent;
import net.springio.conference.shared.domain.AggregateRootWithDomainEvents;
import net.springio.conference.shared.domain.Price;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

@AggregateRoot
public class Session extends AggregateRootWithDomainEvents {
    @Identity
    private final SessionId id;
    private final Title title;
    private final Speakers speakers;

    private Price price;

    Session(SessionId id, Title title, Speakers speakers, Price price) {
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

    public void changePrice(Price price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        this.price = price;
        registerEvent(new PriceChangedEvent(id.uuid(), price));
    }
}
