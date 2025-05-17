package net.springio.conference.session.domain;

import org.jmolecules.ddd.annotation.Repository;

import java.util.stream.Stream;

@Repository
public interface SessionRepository {
    Stream<Session> findAll();

    Stream<Session> findByTitleContainingIgnoreCase(String title);

}