package com.pulsemetrics.pulsemetrics.service;

import com.pulsemetrics.pulsemetrics.dto.request.EntryRequest;
import com.pulsemetrics.pulsemetrics.dto.response.EntryResponse;
import com.pulsemetrics.pulsemetrics.model.entity.Entry;
import com.pulsemetrics.pulsemetrics.model.entity.Metric;
import com.pulsemetrics.pulsemetrics.repository.EntryRepository;
import com.pulsemetrics.pulsemetrics.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final MetricRepository metricRepository;

    public EntryResponse createEntry(Long metricId, EntryRequest request, String username) {
        Metric metric = metricRepository.findById(metricId)
                .orElseThrow(() -> new RuntimeException("Metric not found"));

        if (!metric.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        Entry entry = Entry.builder()
                .value(request.getValue())
                .note(request.getNote())
                .loggedAt(request.getLoggedAt())
                .metric(metric)
                .build();

        return toResponse(entryRepository.save(entry));
    }

    public List<EntryResponse> getEntries(Long metricId, String username) {
        Metric metric = metricRepository.findById(metricId)
                .orElseThrow(() -> new RuntimeException("Metric not found"));

        if (!metric.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        return entryRepository.findByMetricId(metricId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteEntry(Long entryId, String username) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        if (!entry.getMetric().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        entryRepository.delete(entry);
    }

    private EntryResponse toResponse(Entry entry) {
        return EntryResponse.builder()
                .id(entry.getId())
                .value(entry.getValue())
                .note(entry.getNote())
                .loggedAt(entry.getLoggedAt())
                .createdAt(entry.getCreatedAt())
                .build();
    }
}