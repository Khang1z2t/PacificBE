package com.pacific.pacificbe.config;

import com.pacific.pacificbe.model.SystemWallet;
import com.pacific.pacificbe.repository.SystemWalletRepository;
import com.pacific.pacificbe.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DatabaseInitializer {


    private final SystemWalletRepository systemWalletRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            initSystemBalance();
        };
    }

    private void initSystemBalance() {
        String walletID = Constant.SYS_WALLET_ID;
        if (!systemWalletRepository.existsById(walletID)) {
            SystemWallet balance = SystemWallet.builder()
                    .id(walletID)
                    .balance(BigDecimal.ZERO)
                    .build();
            systemWalletRepository.save(balance);
            log.info(">>> DATABASE INIT: Đã tạo ví hệ thống mặc định (Balance: 0)");
        }
    }
}
