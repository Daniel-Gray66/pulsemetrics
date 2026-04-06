package com.pulsemetrics.pulsemetrics.controller;

import com.pulsemetrics.pulsemetrics.dto.request.EntryRequest;
import com.pulsemetrics.pulsemetrics.dto.response.EntryResponse;
import com.pulsemetrics.pulsemetrics.service.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/metrics/{metricId}/entries")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<EntryResponse> createEntry(@PathVariable Long metricId,
                                                      @Valid @RequestBody EntryRequest request,
                                                      Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entryService.createEntry(metricId, request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<EntryResponse>> getEntries(@PathVariable Long metricId,
                                                           Principal principal) {
        return ResponseEntity.ok(entryService.getEntries(metricId, principal.getName()));
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long metricId,
                                             @PathVariable Long entryId,
                                             Principal principal) {
        entryService.deleteEntry(entryId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}