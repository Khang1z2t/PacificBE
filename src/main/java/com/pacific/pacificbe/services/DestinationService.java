package com.pacific.pacificbe.services;

import com.pacific.pacificbe.model.Destination;

import java.util.List;
import java.util.Optional;

public interface DestinationService {
    Optional<Destination> getDestinationById(String id);
    List<Destination> getAllDestinations();
    Destination createDestination(Destination destination);
    Optional<Destination> updateDestination(String id, Destination updatedDestination);
    boolean deleteDestination(String id);
}
