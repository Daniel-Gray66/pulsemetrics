package com.pulsemetrics.pulsemetrics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrendResponse {
    private Double thisWeekAverage;
    private Double lastWeekAverage;
    private Double changePercent;
    private String direction; // "UP", "DOWN", "STABLE"
}