package com.onevoice.ticket.infrastructure;

import com.onevoice.common.enumtype.TicketStatus;
import com.onevoice.ticket.domain.QTicket;
import com.onevoice.ticket.domain.QTicketSeat;
import com.onevoice.ticket.domain.Ticket;
import com.onevoice.ticket.domain.repository.TicketRepository;
import com.onevoice.ticket.infrastructure.jpa.TicketJpaRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

    private final TicketJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Ticket save(Ticket ticket) {
        return jpaRepository.save(ticket);
    }

    @Override
    public Page<Ticket> searchByUserId(UUID userID, Pageable pageable) {

        QTicket ticket = QTicket.ticket;
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), ticket);

        List<Ticket> ticketList = queryFactory
                .selectFrom(ticket)
                .where(ticket.userId.eq(userID),
                        ticket.deletedAt.isNull())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(ticket.count())
                .from(ticket)
                .where(ticket.userId.eq(userID),
                        ticket.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(ticketList,pageable,total != null ? total : 0);
    }

    @Override
    public Page<Ticket> searchTicketByKeyword(UUID ticketID, Pageable pageable, String keyword) {

        QTicket ticket = QTicket.ticket;
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), ticket);

        List<Ticket> ticketList = queryFactory
                .selectFrom(ticket)
                .where(ticket.showName.containsIgnoreCase(keyword),
                        ticket.deletedAt.isNull())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long toal = queryFactory
                .select(ticket.count())
                .from(ticket)
                .where(ticket.showName.containsIgnoreCase(keyword),
                        ticket.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(ticketList,pageable,toal != null ? toal : 0);
    }

    @Override
    public Optional<Ticket> findById(UUID ticketId) {
        QTicket ticket = QTicket.ticket;

        return Optional.ofNullable(queryFactory
                .selectFrom(ticket)
                .where(ticket.id.eq(ticketId),
                        ticket.deletedAt.isNull())
                .fetchFirst());
    }

    @Override
    public Optional<Ticket> findByIdWithJoinSeat(UUID ticketId) {
        QTicket ticket = QTicket.ticket;
        QTicketSeat ticketSeat = QTicketSeat.ticketSeat;

        return Optional.ofNullable(queryFactory
            .selectFrom(ticket)
            .join(ticket.ticketSeatList,ticketSeat).fetchJoin()
            .where(ticket.id.eq(ticketId),
                ticket.deletedAt.isNull())
            .fetchFirst());
    }

    @Override
    public Optional<Ticket> findBySeatIdAndUserId(List<UUID> seatIds, UUID userId) {
        QTicket ticket = QTicket.ticket;
        QTicketSeat ticketSeat = QTicketSeat.ticketSeat;

        return Optional.ofNullable(queryFactory
            .selectFrom(ticket)
            .distinct()
            .join(ticket.ticketSeatList, ticketSeat).fetchJoin()
            .where(ticket.userId.eq(userId),
                ticketSeat.seatId.in(seatIds),
                ticket.status.eq(TicketStatus.WAITING_PAYMENT))
            .fetchFirst());
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort, QTicket ticket) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for(Sort.Order order : sort) {
            Order direction =order.isAscending() ? Order.ASC :Order.DESC;
            String property = order.getProperty().toLowerCase();

            switch (property) {
                case "createdat" -> orderSpecifiers.add(new OrderSpecifier<>(direction, ticket.createdAt));
                case "createdby" -> orderSpecifiers.add(new OrderSpecifier<>(direction, ticket.createdBy));
                case "userid" -> orderSpecifiers.add(new OrderSpecifier<>(direction, ticket.userId));
                case "sessionid" -> orderSpecifiers.add(new OrderSpecifier<>(direction, ticket.sessionId));
                case "showname" ->orderSpecifiers.add(new OrderSpecifier<>(direction,ticket.showName));
                case "status" -> orderSpecifiers.add(new OrderSpecifier<>(direction, ticket.status));
                default -> throw new IllegalArgumentException("정렬 불가능한 필드: "+property+ "\n"
                        + "정렬 가능한 필드들 리스트 : createdAt,createdBy,userId,sessionId,showName,status");
            }
        }
        return orderSpecifiers;
    }
}
