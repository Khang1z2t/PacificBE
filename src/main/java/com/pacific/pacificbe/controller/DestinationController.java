package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.model.Destination;
import com.pacific.pacificbe.services.DestinationService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(UrlMapping.DESTINATION)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DestinationController {
    DestinationService destinationService;

    @GetMapping(UrlMapping.GET_ALL_DESTINATIONS)
    @Operation(summary = "Lấy danh sách tất cả các điểm đến")
    public ResponseEntity<List<Destination>> getAllDestinations() {
        List<Destination> destinations = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinations);
    }

    @PostMapping(UrlMapping.CREATE_DESTINATION)
    @Operation(summary = "Tạo mới một điểm đến")
    public ResponseEntity<Destination> createDestination(@RequestBody Destination destination) {
        Destination createdDestination = destinationService.createDestination(destination);
        return ResponseEntity.ok(createdDestination);
    }

    @GetMapping(UrlMapping.GET_DESTINATION_BY_ID)
    @Operation(summary = "Lấy thông tin của một điểm đến theo ID")
    public ResponseEntity<?> getDestinationById(@PathVariable String id) {
        Optional<Destination> destination = destinationService.getDestinationById(id);
        return destination.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(UrlMapping.UPDATE_DESTINATION)
    @Operation(summary = "Cập nhật thông tin một điểm đến")
    public ResponseEntity<Destination> updateDestination(
            @PathVariable String id, @RequestBody Destination updatedDestination) {
        Optional<Destination> destination = destinationService.updateDestination(id, updatedDestination);
        return destination.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(UrlMapping.DELETE_DESTINATION)
    @Operation(summary = "Xóa một điểm đến theo ID")
    public ResponseEntity<Void> deleteDestination(@PathVariable String id) {
        boolean deleted = destinationService.deleteDestination(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
