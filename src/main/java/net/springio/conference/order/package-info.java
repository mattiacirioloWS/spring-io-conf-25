@BoundedContext(id = "orders")
@ApplicationModule(allowedDependencies = {"session", "session::domain.events", "attendee"})
package net.springio.conference.order;

import org.jmolecules.ddd.annotation.BoundedContext;
import org.springframework.modulith.ApplicationModule;