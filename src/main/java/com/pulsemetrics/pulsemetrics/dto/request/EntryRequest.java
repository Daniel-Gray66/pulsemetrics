package com.pulsemetrics.pulsemetrics.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EntryRequest {

    @NotNull(message = "Value is required")
    private Double value;

    private String note;

    @NotNull(message = "Logged time is required")
    private LocalDateTime loggedAt;
}