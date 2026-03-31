package com.pulsemetrics.pulsemetrics.repository;

import com.pulsemetrics.pulsemetrics.model.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByMetricId(Long metricId);
    List<Entry> findByMetricIdAndLoggedAtBetween(Long metricId, LocalDateTime start, LocalDateTime end);
}