package com.example.priyanka.mapsnearbyplaces.Model;

public class Salesmen {

    private String userCategory;
    private String userId;
    private String companyId;

    public Salesmen() {}

    public Salesmen(String userCategory,String userId,String companyId){
        this.userCategory=userCategory;
        this.userId=userId;
        this.companyId = companyId;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }
}
