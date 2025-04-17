package com.onevoice.ticket.infrastructure.jpa;

import com.onevoice.ticket.domain.TicketSeat;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketSeatJpaRepository extends JpaRepository<TicketSeat, UUID> {

}
