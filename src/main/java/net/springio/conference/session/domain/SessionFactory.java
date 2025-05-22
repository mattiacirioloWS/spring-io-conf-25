package net.springio.conference.session.domain;

import net.springio.conference.shared.domain.Price;
import org.jmolecules.ddd.annotation.Factory;

@Factory
public final class SessionFactory {

    public static Session reconstitute(SessionId id, Title title, Speakers speakers, Price price) {
        return new Session(id, title, speakers, price);
    }
}
