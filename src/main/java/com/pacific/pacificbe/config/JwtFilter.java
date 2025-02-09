package com.pacific.pacificbe.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class JwtFilter extends OncePerRequestFilter {
    Jw
}
