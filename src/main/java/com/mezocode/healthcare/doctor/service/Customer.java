package com.mezocode.healthcare.doctor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Customer {
    private String name;
    private String email;
    private List<Order> orders;
    private boolean discountEligible;

    public Customer(String name, String email) {
        this.name = name;
        setEmail(email); // Use setter to validate upon object creation
        this.orders = new ArrayList<>();
        this.discountEligible = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Additional validation or business logic can go here
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    private boolean isValidEmail(String email) {
        // Simple regex to check email pattern
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public void placeOrder(Order order) {
        // Business logic to place an order
        orders.add(order);
    }

    public void applyDiscount() {
        if (isDiscountEligible()) {
            // Apply discount logic
            // For simplicity, we assume a fixed discount rate
            for (Order order : orders) {
                order.applyDiscount(0.10); // 10% discount
            }
            discountEligible = false;
        } else {
            throw new IllegalStateException("Customer is not eligible for a discount");
        }
    }

    public boolean isDiscountEligible() {
        // Business logic to determine if the customer is eligible for a discount
        // For example, after placing a certain number of orders
        return discountEligible;
    }

    public void updateProfile(String newName, String newEmail) {
        setName(newName);
        setEmail(newEmail);
    }

    // Additional methods and business logic can be added here
}