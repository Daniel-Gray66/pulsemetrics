package com.pulsemetrics.pulsemetrics.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class EntryResponse {
    private Long id;
    private Double value;
    private String note;
    private LocalDateTime loggedAt;
    private LocalDateTime createdAt;
}