package com.cdl.kata.services;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.cdl.kata.dto.CheckoutResponseDto;
import com.cdl.kata.dto.PricingRuleDto;
import com.cdl.kata.enums.DiscountTypeEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    public ResponseEntity<CheckoutResponseDto> calculateTotal(List<String> items, List<PricingRuleDto> pricingRules) {
        // logger.debug("Items: {}, Pricing Rules: {}", items, pricingRules);

        ResponseEntity<String> validationError = validateInput(items, pricingRules);
        if (validationError != null)
            throw new IllegalArgumentException(validationError.getBody());

        Map<String, Long> itemsMap = items.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        logger.debug("Item map: {}", itemsMap);

        CheckoutResponseDto response = new CheckoutResponseDto();

        for (PricingRuleDto rule : pricingRules) {
            String sku = rule.getSku();
            long quantity = itemsMap.getOrDefault(sku, 0L);

            if (quantity == 0)
                continue;

            boolean hasActiveDiscount = isDiscountActive(rule);

            if (hasActiveDiscount && rule.getDiscount().getType() == DiscountTypeEnum.GROUP_DISCOUNT) {
                logger.debug("Applying GROUP_DISCOUNT for SKU {}", sku);

                int groupQty = rule.getDiscount().getQuantity();
                int discountQty = (int) Math.floor((double) quantity / groupQty);
                int remainder = (int) (quantity % groupQty);

                int discountPrice = rule.getDiscount().getPrice();
                int unitPrice = rule.getUnitPrice();

                int total = discountQty * discountPrice + remainder * unitPrice;
                int subtotal = (int) quantity * unitPrice;

                response.setTotal(response.getTotal() + total);
                response.setSubtotal(response.getSubtotal() + subtotal);
            } else {
                logger.debug("No active discount for SKU {}", sku);

                int total = (int) (quantity * rule.getUnitPrice());
                response.setTotal(response.getTotal() + total);
                response.setSubtotal(response.getSubtotal() + total);
            }
        }
        response.setDiscountTotal(response.getSubtotal() - response.getTotal());

        return ResponseEntity.ok(response);
    }

    private boolean isDiscountActive(PricingRuleDto rule) {
        if (rule.getDiscount() == null || !Boolean.TRUE.equals(rule.getDiscount().getIsActive())) {
            return false;
        }

        // Get current date
        LocalDate now = LocalDate.now();

        // Check if dates are present and valid
        return rule.getDiscount().getValidFrom()
                .map(from -> !now.isBefore(from))
                .orElse(true) &&
                rule.getDiscount().getValidTo()
                        .map(to -> !now.isAfter(to))
                        .orElse(true);
    }

    private ResponseEntity<String> validateInput(List<String> items, List<PricingRuleDto> rules) {
        if (items == null || items.isEmpty())
            return ResponseEntity.badRequest().body("Items must not be null or empty");
        if (rules == null || rules.isEmpty())
            return ResponseEntity.badRequest().body("Pricing rules must not be null or empty");

        boolean hasInvalidItems = items.stream()
                .anyMatch(item -> item == null || item.length() != 1 || !item.matches("[A-Z]"));
        if (hasInvalidItems)
            return ResponseEntity.badRequest().body("Items must be single uppercase letters A-Z");

        boolean hasInvalidRules = rules.stream().anyMatch(rule -> rule == null || rule.getSku() == null
                || rule.getSku().length() != 1 || !rule.getSku().matches("[A-Z]"));
        if (hasInvalidRules)
            return ResponseEntity.badRequest().body("Pricing rules must contain valid single-character SKUs");

        return null;
    }
}
