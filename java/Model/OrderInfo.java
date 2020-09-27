package com.example.priyanka.mapsnearbyplaces.Model;

import java.sql.Timestamp;

public class OrderInfo {
    private String custName;
    private Long orderQuantity;
    private Timestamp orderDate;
    private Long totalPrice;

    public OrderInfo(){ }

    public OrderInfo(String custName, Long orderQuantity, Timestamp orderDate, Long totalPrice) {
        this.custName = custName;
        this.orderQuantity = orderQuantity;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public String getCustName() {
        return custName;
    }

    public Long getOrderQuantity() {
        return orderQuantity;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}
