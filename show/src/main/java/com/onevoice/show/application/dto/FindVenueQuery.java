package com.onevoice.show.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindVenueQuery(
    UUID venueId,
    String name,
    String location,
    String description,
    Integer totalSeatCount,
    LocalDateTime createAt,
    UUID createdBy,
    LocalDateTime updateAt,
    UUID updatedBy,
    LocalDateTime deleteAt,
    UUID deletedBy
) {
    
}
