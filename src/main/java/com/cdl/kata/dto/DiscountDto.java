package com.cdl.kata.dto;

import com.cdl.kata.enums.DiscountTypeEnum;
import java.time.LocalDate;
import java.util.Optional;

public class DiscountDto {
    public DiscountDto() {
    }
    public DiscountDto(int quantity, int price, String description, LocalDate validFrom, 
        LocalDate validTo, DiscountTypeEnum type, boolean isActive) {
            
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.validFrom = Optional.ofNullable(validFrom);
        this.validTo = Optional.ofNullable(validTo);
        this.type = type;
        this.isActive = isActive;
    }
    
    
    private int quantity;
    private int price;
    private String description;
    private Optional<LocalDate> validFrom = Optional.empty();
    private Optional<LocalDate> validTo = Optional.empty();
    private DiscountTypeEnum type;
    private boolean isActive = false;
    
    public String getDescription() {
        return description;
    }
    
    public Optional<LocalDate> getValidFrom() {
        return validFrom;
    }
    
    public Optional<LocalDate> getValidTo() {
        return validTo;
    }
    
    public int getPrice() {
        return price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public DiscountTypeEnum getType() {
        return type;
    }
    
    public boolean getIsActive() {
        return isActive;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = Optional.ofNullable(validFrom);
    }
    
    public void setValidTo(LocalDate validTo) {
        this.validTo = Optional.ofNullable(validTo);
    }
    
    public void setType(DiscountTypeEnum type) {
        this.type = type;
    }
    
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}