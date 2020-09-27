package com.example.priyanka.mapsnearbyplaces.Model;

public class CustomerInfo {
     private String custName;

     public CustomerInfo(){ }

     public CustomerInfo(String customerName){
         this.custName=custName;
     }

    public String getCustName() {
        return custName;
    }
}
