package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.model.Destination;
import com.pacific.pacificbe.repository.DestinationRepository;
import com.pacific.pacificbe.services.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository destinationRepository;

    @Override
    public Optional<Destination> getDestinationById(String id) {
        return destinationRepository.findById(id);
    }
}
