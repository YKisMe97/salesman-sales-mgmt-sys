package com.example.priyanka.mapsnearbyplaces.SalesManager;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.Date;

public class SalesmanLocation extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    double end_latitude,end_longitude;
    double lat;
    double lng;
    LatLng latlng;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private String currrentUser =  FirebaseAuth.getInstance().getCurrentUser().getUid();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesman_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        db.collection("company_user").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("", "Error : " + e.getMessage());
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        doc.getDocument().getReference().collection("attendance").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.d("", "Error : " + e.getMessage());
                                }

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        Log.d("attendace Name: ", doc.getDocument().getId());
                                        GeoPoint salesman_location = doc.getDocument().getGeoPoint("location");
                                        Timestamp datetime = doc.getDocument().getTimestamp("datetime");
                                        Date date =datetime.toDate();
                                        Date today = Timestamp.now().toDate();
                                        String stringDate = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
                                        String stringToday = DateFormat.getDateInstance(DateFormat.SHORT).format(today);
                                        String snippet = "Customer Details\n" + "Salesman Name: " + "\n" +
                                                "Check in Time: " +stringDate + "\n";
                                        lat = salesman_location.getLatitude();
                                        lng = salesman_location.getLongitude();
                                        latlng = new LatLng(lat, lng);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latlng);
                                        markerOptions.draggable(true);
                                        markerOptions.title("");
                                        markerOptions.snippet(snippet);
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                        if(stringDate.equals(stringToday)){
                                        mMap.addMarker(markerOptions);}
                                        //Toast.makeText(MapsActivity.this,"Document found ",Toast.LENGTH_LONG).show();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                                        mMap.moveCamera(CameraUpdateFactory.zoomTo(8));
                                    }
                                }

                            }
                        });
                    }
                }
            }
        });
    }
    public void onLocationChanged(Location location) {

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(false);
        end_latitude = marker.getPosition().latitude;
        end_longitude =  marker.getPosition().longitude;

        Log.d("end_lat",""+end_latitude);
        Log.d("end_lng",""+end_longitude);
        return false;
    }
}
