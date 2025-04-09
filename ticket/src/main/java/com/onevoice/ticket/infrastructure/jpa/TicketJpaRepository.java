package com.onevoice.ticket.infrastructure.jpa;

import com.onevoice.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketJpaRepository extends JpaRepository<Ticket, UUID> {

}
