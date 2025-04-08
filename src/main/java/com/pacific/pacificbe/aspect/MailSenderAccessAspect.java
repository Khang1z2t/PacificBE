package com.pacific.pacificbe.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MailSenderAccessAspect {
    @Before("@annotation(com.pacific.pacificbe.annotations.MailSchedulerOnly)")
    public void restrictSendMethods(JoinPoint joinPoint) {
        String callerClass = joinPoint.getThis().getClass().getName();
        String targetClass = joinPoint.getTarget().getClass().getName();

        // Cho phép nếu caller hoặc target liên quan đến scheduler
        boolean isAllowed = callerClass.startsWith("com.pacific.pacificbe.scheduler") ||
                targetClass.startsWith("com.pacific.pacificbe.scheduler") ||
                callerClass.contains("$$SpringCGLIB$$"); // Bỏ qua proxy CGLIB

        if (!isAllowed) {
            log.error("Không thể gọi phương thức: {} từ class: {}, target class: {}",
                    joinPoint.getSignature().getName(), callerClass, targetClass);
            throw new SecurityException("Chỉ có class trong 'com.pacific.pacificbe.scheduler' mới có thể gọi phương thức");
        }
        log.info("Phương thức '{}' được gọi hợp lệ", joinPoint.getSignature().getName());
    }
}
