package com.example.priyanka.mapsnearbyplaces.Model;

public class CompanyModel {
    private String companyId;
    private String companyName;
    private String companyType;
    private String passcode;

    public CompanyModel(){}

    public CompanyModel(String companyId, String companyName, String companyType, String passcode) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyType = companyType;
        this.passcode = passcode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public String getPasscode() {
        return passcode;
    }
}
