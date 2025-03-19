package com.pacific.pacificbe.services;

import com.pacific.pacificbe.model.Destination;

import java.util.Optional;

public interface DestinationService {
    Optional<Destination> getDestinationById(String id);
}
