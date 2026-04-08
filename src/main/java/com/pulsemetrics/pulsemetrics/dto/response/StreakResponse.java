package com.pulsemetrics.pulsemetrics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StreakResponse {
    private int currentStreak;
    private int longestStreak;
    private Double goalValue;
    private String goalDirection;
}