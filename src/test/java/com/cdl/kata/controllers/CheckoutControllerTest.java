package com.cdl.kata.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.cdl.kata.dto.CheckoutRequestDto;
import com.cdl.kata.dto.CheckoutResponseDto;
import com.cdl.kata.services.CheckoutService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CheckoutController.class)
public class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCheckout() throws Exception {
        // Create a sample request
        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setItems(Arrays.asList("A", "B", "C"));
        
        // Mock the service response
        CheckoutResponseDto responseDto = new CheckoutResponseDto();
        responseDto.setTotal(100);
        when(checkoutService.calculateTotal(any(), any())).thenReturn(ResponseEntity.ok(responseDto));
        
        // Perform the request and print the results
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}