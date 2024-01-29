package com.mezocode.healthcare.doctor.service;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<Item> items;
    private double totalCost;

    public Order() {
        this.items = new ArrayList<>();
        this.totalCost = 0.0;
    }

    public void addItem(Item item) {
        items.add(item);
        totalCost += item.getPrice();
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void applyDiscount(double discountRate) {
        totalCost -= totalCost * discountRate;
    }

    // Additional order-related methods and logic
}