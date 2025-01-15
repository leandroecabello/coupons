package com.coupon.challenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CouponRequest {

    @JsonProperty("item_ids")
    private List<String> itemIds;
    @JsonProperty("amount")
    private double amount;

    // Getters y Setters
    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}