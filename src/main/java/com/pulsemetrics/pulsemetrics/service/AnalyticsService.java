package com.pulsemetrics.pulsemetrics.service;

import com.pulsemetrics.pulsemetrics.dto.response.MetricSummaryResponse;
import com.pulsemetrics.pulsemetrics.dto.response.StreakResponse;
import com.pulsemetrics.pulsemetrics.dto.response.TrendResponse;
import com.pulsemetrics.pulsemetrics.model.entity.Entry;
import com.pulsemetrics.pulsemetrics.model.entity.Metric;
import com.pulsemetrics.pulsemetrics.repository.EntryRepository;
import com.pulsemetrics.pulsemetrics.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final EntryRepository entryRepository;
    private final MetricRepository metricRepository;

    public MetricSummaryResponse getSummary(Long metricId, String username,
                                             LocalDateTime from, LocalDateTime to) {
        Metric metric = getVerifiedMetric(metricId, username);

        List<Entry> entries = (from != null && to != null)
                ? entryRepository.findByMetricIdAndLoggedAtBetween(metricId, from, to)
                : entryRepository.findByMetricId(metricId);

        if (entries.isEmpty()) {
            return MetricSummaryResponse.builder()
                    .average(0.0).min(0.0).max(0.0).totalEntries(0L)
                    .from(from != null ? from.toString() : "all time")
                    .to(to != null ? to.toString() : "all time")
                    .build();
        }

        double avg = entries.stream().mapToDouble(Entry::getValue).average().orElse(0);
        double min = entries.stream().mapToDouble(Entry::getValue).min().orElse(0);
        double max = entries.stream().mapToDouble(Entry::getValue).max().orElse(0);

        return MetricSummaryResponse.builder()
                .average(Math.round(avg * 100.0) / 100.0)
                .min(min)
                .max(max)
                .totalEntries((long) entries.size())
                .from(from != null ? from.toString() : "all time")
                .to(to != null ? to.toString() : "all time")
                .build();
    }

    public StreakResponse getStreak(Long metricId, String username) {
        Metric metric = getVerifiedMetric(metricId, username);

        if (metric.getGoalValue() == null || metric.getGoalDirection() == null) {
            throw new RuntimeException("No goal set for this metric");
        }

        List<Entry> entries = entryRepository.findByMetricId(metricId);

        // Group entries by date, taking the average value per day
        Map<LocalDate, Double> dailyAverages = entries.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getLoggedAt().toLocalDate(),
                        Collectors.averagingDouble(Entry::getValue)
                ));

        List<LocalDate> sortedDates = dailyAverages.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        int currentStreak = 0;
        int longestStreak = 0;
        int tempStreak = 0;
        LocalDate yesterday = LocalDate.now().minusDays(1);

        for (int i = sortedDates.size() - 1; i >= 0; i--) {
            LocalDate date = sortedDates.get(i);
            double value = dailyAverages.get(date);
            boolean goalMet = isGoalMet(value, metric.getGoalValue(),
                    metric.getGoalDirection().name());

            if (goalMet) {
                tempStreak++;
                if (i == sortedDates.size() - 1 &&
                        (date.equals(LocalDate.now()) || date.equals(yesterday))) {
                    currentStreak = tempStreak;
                }
            } else {
                longestStreak = Math.max(longestStreak, tempStreak);
                tempStreak = 0;
                if (currentStreak == 0) break;
            }
        }

        longestStreak = Math.max(longestStreak, tempStreak);

        return StreakResponse.builder()
                .currentStreak(currentStreak)
                .longestStreak(longestStreak)
                .goalValue(metric.getGoalValue())
                .goalDirection(metric.getGoalDirection().name())
                .build();
    }

    public TrendResponse getTrend(Long metricId, String username) {
        getVerifiedMetric(metricId, username);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        LocalDateTime twoWeeksAgo = now.minusWeeks(2);

        List<Entry> thisWeek = entryRepository
                .findByMetricIdAndLoggedAtBetween(metricId, oneWeekAgo, now);
        List<Entry> lastWeek = entryRepository
                .findByMetricIdAndLoggedAtBetween(metricId, twoWeeksAgo, oneWeekAgo);

        double thisAvg = thisWeek.stream()
                .mapToDouble(Entry::getValue).average().orElse(0);
        double lastAvg = lastWeek.stream()
                .mapToDouble(Entry::getValue).average().orElse(0);

        double changePercent = lastAvg == 0 ? 0
                : Math.round(((thisAvg - lastAvg) / lastAvg) * 100 * 100.0) / 100.0;

        String direction = changePercent > 1 ? "UP"
                : changePercent < -1 ? "DOWN" : "STABLE";

        return TrendResponse.builder()
                .thisWeekAverage(Math.round(thisAvg * 100.0) / 100.0)
                .lastWeekAverage(Math.round(lastAvg * 100.0) / 100.0)
                .changePercent(changePercent)
                .direction(direction)
                .build();
    }

    private Metric getVerifiedMetric(Long metricId, String username) {
        Metric metric = metricRepository.findById(metricId)
                .orElseThrow(() -> new RuntimeException("Metric not found"));
        if (!metric.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }
        return metric;
    }

    private boolean isGoalMet(double value, double goal, String direction) {
        return direction.equals("ABOVE") ? value >= goal : value <= goal;
    }
}