package com.fintech.fintechapp.controller;

import com.fintech.fintechapp.repository.WalletRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.fintech.fintechapp.utils.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/mpesa")
public class MpesaCallbackController {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/c2b/confirmation")
    public ResponseEntity<String> handleC2BConfirmation(
            @RequestHeader(value = "Authorization", required=false) String authHeader,
            @RequestBody Map<String, Object> payload) {
        System.out.println("Received Mpesa confirmation: " + payload);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid authorization header");
        }

        String token = authHeader.substring(7); // I'll remove the bearer
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        //Extract and handle payment
        String billRefNumber = (String) payload.get("BillRefNumber");
        String amountStr = String.valueOf(payload.get("TransAmount"));
        Integer walletId = findWalletIdByBillRefNumber(billRefNumber);

        if (walletId != null) {
            walletRepository.updateWalletBalanceAfterMpesaCallback(walletId, amountStr);
            return ResponseEntity.ok("Confirmation processed");
        } else {
            return ResponseEntity.badRequest().body("Wallet not found");
        }
    }

    private Integer findWalletIdByBillRefNumber(String billRefNumber) {
        return 1; // this is a dummy wallet id;
    }
}