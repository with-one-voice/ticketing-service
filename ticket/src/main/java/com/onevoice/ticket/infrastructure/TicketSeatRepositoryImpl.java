package com.onevoice.ticket.infrastructure;

import com.onevoice.ticket.domain.TicketSeat;
import com.onevoice.ticket.domain.repository.TicketSeatRepository;
import com.onevoice.ticket.infrastructure.jpa.TicketSeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketSeatRepositoryImpl implements TicketSeatRepository {

    private final TicketSeatJpaRepository jpaRepository;

    @Override
    public TicketSeat save(TicketSeat ticketSeat) {
        return jpaRepository.save(ticketSeat);
    }
}
