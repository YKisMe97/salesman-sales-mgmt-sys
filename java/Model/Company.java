package com.example.priyanka.mapsnearbyplaces.Model;

public class Company {
    private String companyId;
    private String userCategory;
    private String userId;

    public Company(){}

    public Company(String companyId, String userCategory, String userId) {
        this.companyId = companyId;
        this.userCategory = userCategory;
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return userCategory;
    }

    public String getCompanyType() {
        return userId;
    }

}
