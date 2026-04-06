package com.pulsemetrics.pulsemetrics.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MetricResponse {
    private Long id;
    private String name;
    private String description;
    private String unit;
    private LocalDateTime createdAt;
}