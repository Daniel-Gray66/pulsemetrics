package com.pulsemetrics.pulsemetrics.controller;

import com.pulsemetrics.pulsemetrics.dto.response.MetricSummaryResponse;
import com.pulsemetrics.pulsemetrics.dto.response.StreakResponse;
import com.pulsemetrics.pulsemetrics.dto.response.TrendResponse;
import com.pulsemetrics.pulsemetrics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/metrics/{metricId}/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/summary")
    public ResponseEntity<MetricSummaryResponse> getSummary(
            @PathVariable Long metricId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Principal principal) {
        return ResponseEntity.ok(
                analyticsService.getSummary(metricId, principal.getName(), from, to));
    }

    @GetMapping("/streak")
    public ResponseEntity<StreakResponse> getStreak(
            @PathVariable Long metricId,
            Principal principal) {
        return ResponseEntity.ok(
                analyticsService.getStreak(metricId, principal.getName()));
    }

    @GetMapping("/trend")
    public ResponseEntity<TrendResponse> getTrend(
            @PathVariable Long metricId,
            Principal principal) {
        return ResponseEntity.ok(
                analyticsService.getTrend(metricId, principal.getName()));
    }
}