package com.pulsemetrics.pulsemetrics.repository;

import com.pulsemetrics.pulsemetrics.model.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByUserId(Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
}