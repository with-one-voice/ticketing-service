package com.onevoice.venue.domain;

import java.util.UUID;

import com.onevoice.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="p_venues")
public class Venue extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="venue_id")
	private UUID id;

	@Column(nullable = false, length = 300)
	private String name;

	@Column(nullable = false, length = 300)
	private String location;

	@Column(nullable = false, length = 300)
	private String description;

	@Column(nullable = false, name = "total_seat_count")
	private Integer totalSeatCount;

}
