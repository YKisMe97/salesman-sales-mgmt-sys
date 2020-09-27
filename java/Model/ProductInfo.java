package com.example.priyanka.mapsnearbyplaces.Model;

public class ProductInfo {
    private String productName;
    private Long quantity;

    public ProductInfo(){ }

    public ProductInfo(String productName, Long quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public Long getQuantity() {
        return quantity;
    }


}
