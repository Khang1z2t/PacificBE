package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.model.TourDetail;
import com.pacific.pacificbe.services.TourDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TourDetailServiceImpl implements TourDetailService {

}
