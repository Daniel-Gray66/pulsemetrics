package com.pulsemetrics.pulsemetrics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricSummaryResponse {
    private Double average;
    private Double min;
    private Double max;
    private Long totalEntries;
    private String from;
    private String to;
}