package com.pulsemetrics.pulsemetrics.controller;

import com.pulsemetrics.pulsemetrics.dto.request.MetricRequest;
import com.pulsemetrics.pulsemetrics.dto.response.MetricResponse;
import com.pulsemetrics.pulsemetrics.service.MetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;

    @PostMapping
    public ResponseEntity<MetricResponse> createMetric(@Valid @RequestBody MetricRequest request,
                                                        Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(metricService.createMetric(request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<MetricResponse>> getMetrics(Principal principal) {
        return ResponseEntity.ok(metricService.getUserMetrics(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetricResponse> getMetric(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(metricService.getMetric(id, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id, Principal principal) {
        metricService.deleteMetric(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}