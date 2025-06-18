package com.cdl.kata.dto;

import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckoutRequestDto {
    private List<String> items;
    
    @JsonProperty("pricingRules")
    private List<PricingRuleDto> pricingRules;
    
    private static final Pattern VALID_ITEMS = Pattern.compile("^[A-D]$");
    
    public CheckoutRequestDto() {}
    
    public CheckoutRequestDto(List<String> items, List<PricingRuleDto> pricingRules) {
        setItems(items);
        this.pricingRules = pricingRules;
    }
    
    @Override
    public String toString() {
        return "CheckoutRequestDto{" +
                "items=" + items +
                ", pricingRules=" + pricingRules +
                '}';
    }
    
    public List<String> getItems() {
        return items;
    }
    
    public void setItems(List<String> items) {
        if (items != null) {
            for (String item : items) {
                if (item == null || !VALID_ITEMS.matcher(item).matches()) {
                    throw new IllegalArgumentException("Items can only be A, B, C, or D");
                }
            }
        }
        this.items = items;
    }
    
    public List<PricingRuleDto> getPricingRules() {
        return pricingRules;
    }
    
    public void setPricingRules(List<PricingRuleDto> pricingRules) {
        this.pricingRules = pricingRules;
    }
}
