package com.cdl.kata.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdl.kata.common.ApiResponse;
import com.cdl.kata.dto.CheckoutRequestDto;
import com.cdl.kata.dto.CheckoutResponseDto;
import com.cdl.kata.services.CheckoutService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class CheckoutController {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<CheckoutResponseDto>> checkout(@RequestBody CheckoutRequestDto request) {
        try {
            ResponseEntity<CheckoutResponseDto> response = checkoutService.calculateTotal(request.getItems(), request.getPricingRules());
            return ResponseEntity.ok(ApiResponse.success((CheckoutResponseDto) response.getBody()));
        } catch (Exception e) {
            logger.error("Error processing checkout request", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}