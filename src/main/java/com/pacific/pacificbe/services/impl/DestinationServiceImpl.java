package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.model.Destination;
import com.pacific.pacificbe.repository.DestinationRepository;
import com.pacific.pacificbe.services.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository destinationRepository;

    @Override
    public Optional<Destination> getDestinationById(String id) {
        return destinationRepository.findById(id);
    }

    @Override
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    @Override
    public Destination createDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    @Override
    public Optional<Destination> updateDestination(String id, Destination updatedDestination) {
        return destinationRepository.findById(id).map(existingDestination -> {
            existingDestination.setCity(updatedDestination.getCity());
            existingDestination.setCountry(updatedDestination.getCountry());
            existingDestination.setFullAddress(updatedDestination.getFullAddress());
            existingDestination.setName(updatedDestination.getName());
            existingDestination.setRegion(updatedDestination.getRegion());
            return destinationRepository.save(existingDestination);
        });
    }

    @Override
    public boolean deleteDestination(String id) {
        if (destinationRepository.existsById(id)) {
            destinationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
