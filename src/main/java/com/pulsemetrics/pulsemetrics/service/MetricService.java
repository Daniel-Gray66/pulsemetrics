package com.pulsemetrics.pulsemetrics.service;

import com.pulsemetrics.pulsemetrics.dto.request.MetricRequest;
import com.pulsemetrics.pulsemetrics.dto.response.MetricResponse;
import com.pulsemetrics.pulsemetrics.model.entity.Metric;
import com.pulsemetrics.pulsemetrics.model.entity.User;
import com.pulsemetrics.pulsemetrics.repository.MetricRepository;
import com.pulsemetrics.pulsemetrics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final MetricRepository metricRepository;
    private final UserRepository userRepository;

    public MetricResponse createMetric(MetricRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (metricRepository.existsByNameAndUserId(request.getName(), user.getId())) {
            throw new RuntimeException("Metric with this name already exists");
        }

        Metric metric = Metric.builder()
                .name(request.getName())
                .description(request.getDescription())
                .unit(request.getUnit())
                .user(user)
                .build();

        return toResponse(metricRepository.save(metric));
    }

    public List<MetricResponse> getUserMetrics(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return metricRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MetricResponse getMetric(Long id, String username) {
        Metric metric = metricRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metric not found"));

        if (!metric.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(metric);
    }

    public void deleteMetric(Long id, String username) {
        Metric metric = metricRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metric not found"));

        if (!metric.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        metricRepository.delete(metric);
    }

    private MetricResponse toResponse(Metric metric) {
        return MetricResponse.builder()
                .id(metric.getId())
                .name(metric.getName())
                .description(metric.getDescription())
                .unit(metric.getUnit())
                .createdAt(metric.getCreatedAt())
                .build();
    }
}