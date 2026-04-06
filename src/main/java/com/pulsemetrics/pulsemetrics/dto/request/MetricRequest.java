package com.pulsemetrics.pulsemetrics.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MetricRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Unit is required")
    private String unit;
}