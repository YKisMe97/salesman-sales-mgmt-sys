package com.example.priyanka.mapsnearbyplaces.Model;

public class ManagerDashModel {
    private String head,sub;
    private int image;

    public ManagerDashModel() {
    }

    public ManagerDashModel(String head, String sub, int image) {
        this.head = head;
        this.sub = sub;
        this.image = image;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
