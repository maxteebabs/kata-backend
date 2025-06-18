package com.cdl.kata.dto;

public class CheckoutResponseDto {
    public CheckoutResponseDto() {
    }
    
    public CheckoutResponseDto(double subtotal, double total, double discountTotal) {
        this.total = total;
        this.subtotal = subtotal;
        this.discountTotal = discountTotal;
    }
    
    public double getTotal() {
        return total;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public double getDiscountTotal() {
        return discountTotal;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    public void setDiscountTotal(double discountTotal) {
        this.discountTotal = discountTotal;
    }
    
    private double total = 0;
    private double subtotal = 0;
    private double discountTotal = 0;
}
