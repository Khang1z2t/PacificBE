package com.pacific.pacificbe.services;

import com.pacific.pacificbe.config.VNPAYConfig;
import com.pacific.pacificbe.dto.request.CheckOutRequest;
import com.pacific.pacificbe.dto.request.VNPAYRequest;
import com.pacific.pacificbe.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


public interface VNPAYService {

    String createOrder(HttpServletRequest request, VNPAYRequest vnpayRequest, String redirectTo);

//    String createOrder(HttpServletRequest request, int amount, String orderInfor, String urlReturn, String userId);

    public int orderReturn(HttpServletRequest request);

    RedirectView processVnpayReturn(CheckOutRequest request);
}
