package com.cdl.kata.controllers;

import com.cdl.kata.dto.CheckoutRequestDto;
import com.cdl.kata.dto.CheckoutResponseDto;
import com.cdl.kata.dto.PricingRuleDto;
import com.cdl.kata.services.CheckoutService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCheckoutSuccess() throws Exception {
        // Prepare mock response
        CheckoutResponseDto mockResponse = new CheckoutResponseDto();
        mockResponse.setTotal(100);
        mockResponse.setSubtotal(120);
        mockResponse.setDiscountTotal(20);

        Mockito.when(checkoutService.calculateTotal(any(), any()))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Prepare request body
        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setItems(List.of("A", "A", "B"));

        PricingRuleDto rule = new PricingRuleDto();
        rule.setSku("A");
        rule.setUnitPrice(50);
        rule.setDiscount(null); // No discount for this test

        request.setPricingRules(List.of(rule));

        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(100))
                .andExpect(jsonPath("$.data.subtotal").value(120))
                .andExpect(jsonPath("$.data.discountTotal").value(20));
    }

    @Test
    void testCheckoutInvalidItems() throws Exception {
        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setItems(Collections.emptyList()); // Invalid
        request.setPricingRules(List.of());

        Mockito.when(checkoutService.calculateTotal(any(), any()))
                .thenThrow(new IllegalArgumentException("Items must not be null or empty"));

        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Items must not be null or empty"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
