package com.cdl.kata.dto;

public class PricingRuleDto {
    private String sku;
    private int unitPrice;
    private int quantity;
    private String unit;
    private DiscountDto discount;
    
    public PricingRuleDto() {}

    public PricingRuleDto(String sku, int quantity, int unitPrice, String unit, DiscountDto discount) {
        this.sku = sku;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.unit = unit;
        this.discount = discount;
    }
    
    public String getUnit() {
        return unit;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getUnitPrice() {
        return unitPrice;
    }
    public String getSku() {
        return sku;
    }
    public DiscountDto getDiscount() {
        return discount;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public void setDiscount(DiscountDto discount) {
        this.discount = discount;
    }   
}