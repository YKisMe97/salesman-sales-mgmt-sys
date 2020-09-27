package com.example.priyanka.mapsnearbyplaces.Model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class VisitPlanInfo {
    private ArrayList<GeoPoint> visitPlanRoute;

    public VisitPlanInfo(){}

    public VisitPlanInfo(ArrayList<GeoPoint> visitPlanRoute) {
        this.visitPlanRoute = visitPlanRoute;
    }

    public ArrayList<GeoPoint> getVisitPlanRoute() {
        return visitPlanRoute;
    }

}
