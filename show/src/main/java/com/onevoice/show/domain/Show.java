package com.onevoice.show.domain;

import com.onevoice.common.entity.BaseEntity;
import com.onevoice.show.presentation.dto.request.UpdateShowRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_shows")
@SQLRestriction("deleted_at IS NULL")
public class Show extends BaseEntity {

    @Id
    @Column(name = "show_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "venue_id")
    private UUID venueId;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(nullable = false, length = 100)
    private String artist;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShowCategory category;

    @Column(name = "poster_url", columnDefinition = "TEXT")
    private String posterUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, name = "ticketing_start_time")
    private LocalDateTime ticketingStartTime;

    @Column(nullable = false, name = "ticketing_end_time")
    private LocalDateTime ticketingEndTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    public void addSession(Session session) {
        sessions.add(session);
        session.setShow(this);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        session.setShow(null);
    }

    public void update(UpdateShowRequestDto requestDto) {
        this.artist = requestDto.artist();
        this.category = requestDto.category();
        this.posterUrl = requestDto.posterUrl();
        this.description = requestDto.description();
        this.ticketingStartTime = requestDto.ticketingStartTime();
        this.ticketingEndTime = requestDto.ticketingEndTime();
    }
}
