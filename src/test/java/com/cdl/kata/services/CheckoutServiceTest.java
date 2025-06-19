package com.cdl.kata.services;

import com.cdl.kata.dto.CheckoutResponseDto;
import com.cdl.kata.common.ApiResponse;
import com.cdl.kata.dto.CheckoutRequestDto;
import com.cdl.kata.dto.PricingRuleDto;
import com.cdl.kata.dto.DiscountDto;
import com.cdl.kata.enums.DiscountTypeEnum;
import com.cdl.kata.services.CheckoutService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutServiceTest {

    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService();
    }

    @Test
    void testCalculateTotal_noDiscounts() {
        PricingRuleDto ruleA = new PricingRuleDto();
        ruleA.setSku("A");
        ruleA.setUnitPrice(50);
        ruleA.setDiscount(null);

        ResponseEntity<?> response = checkoutService.calculateTotal(
                List.of("A", "A", "B"),
                List.of(ruleA));

        CheckoutResponseDto result = (CheckoutResponseDto) response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getSubtotal()).isEqualTo(100); // 2xA @ 50 each
        assertThat(result.getTotal()).isEqualTo(100);
        assertThat(result.getDiscountTotal()).isEqualTo(0);
    }

    @Test
    void testCalculateTotal_withGroupDiscount() {
        DiscountDto discount = new DiscountDto();
        discount.setType(DiscountTypeEnum.GROUP_DISCOUNT);
        discount.setIsActive(true);
        discount.setQuantity(3);
        discount.setPrice(130);
        discount.setValidFrom(LocalDate.now().minusDays(1));
        discount.setValidTo((LocalDate.now().plusDays(1)));

        PricingRuleDto rule = new PricingRuleDto();
        rule.setSku("A");
        rule.setUnitPrice(50);
        rule.setDiscount(discount);

        ResponseEntity<?> response = checkoutService.calculateTotal(
                List.of("A", "A", "A", "A"),
                List.of(rule));

        CheckoutResponseDto result = (CheckoutResponseDto) response.getBody();
        assertThat(result.getTotal()).isEqualTo(180);
        assertThat(result.getSubtotal()).isEqualTo(200);
        assertThat(result.getDiscountTotal()).isEqualTo(20);
    }

    @Test
    void testCalculateTotal_discountOutsideValidDate() {
        DiscountDto discount = new DiscountDto();
        discount.setType(DiscountTypeEnum.GROUP_DISCOUNT);
        discount.setIsActive(true);
        discount.setQuantity(2);
        discount.setPrice(80);
        discount.setValidFrom(LocalDate.now().minusDays(10));
        discount.setValidTo(LocalDate.now().minusDays(1)); // expired

        PricingRuleDto rule = new PricingRuleDto();
        rule.setSku("A");
        rule.setUnitPrice(50);
        rule.setDiscount(discount);

        ResponseEntity<?> response = checkoutService.calculateTotal(
                List.of("A", "A"),
                List.of(rule));

        CheckoutResponseDto result = (CheckoutResponseDto) response.getBody();
        assertThat(result.getTotal()).isEqualTo(100);
        assertThat(result.getDiscountTotal()).isEqualTo(0);
    }

    @Test
    void testCalculateTotal_invalidItems() {

        List<String> invalidItems = Collections.emptyList(); // empty
        List<PricingRuleDto> rules = List.of();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.calculateTotal(invalidItems, rules);
        });
        assertThat(exception.getMessage()).isEqualTo("Items must not be null or empty");

    }

    @Test
    void testCalculateTotal_invalidSkuFormat() {
        PricingRuleDto badRule = new PricingRuleDto();
        badRule.setSku("invalidSKU");
        badRule.setUnitPrice(50);
        badRule.setDiscount(null);
        
         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.calculateTotal(
                List.of("A"),
                List.of(badRule)
            );
        });
        assertThat(exception.getMessage()).isEqualTo("Pricing rules must contain valid single-character SKUs");

    }
}
