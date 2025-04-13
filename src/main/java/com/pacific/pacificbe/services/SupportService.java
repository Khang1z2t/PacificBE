package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.SupportRequest;
import com.pacific.pacificbe.dto.request.UpdateStatusSupportRequest;
import com.pacific.pacificbe.dto.response.SupportResponse;
import com.pacific.pacificbe.model.Support;

import java.util.List;
import java.util.Optional;

public interface SupportService {

    SupportResponse getSupportById(String id);

    List<SupportResponse> getAllSupports();

    SupportResponse updateStatus(String id, UpdateStatusSupportRequest request);

    Optional<SupportResponse> getSupportByEmail(String email);

    SupportResponse createSupport(SupportRequest request);

    Optional<Support> getUserById(String userId);

    void sendSupportResponseEmail(Support support);
}
