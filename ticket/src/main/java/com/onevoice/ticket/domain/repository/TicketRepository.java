package com.onevoice.ticket.domain.repository;

import com.onevoice.ticket.domain.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {
    Ticket save(Ticket ticket);

    Page<Ticket> searchByUserId(UUID userID, Pageable pageable);

    Page<Ticket> searchTicketByKeyword(UUID ticketID, Pageable pageable, String keyword);

    Optional<Ticket> findById(UUID ticketId);
}
