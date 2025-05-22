package net.springio.conference.session;

import org.jmolecules.architecture.layered.InterfaceLayer;

import java.util.Optional;
import java.util.UUID;

@InterfaceLayer
public interface FindSessions {
    Optional<SessionDto> findById(UUID id);
}
