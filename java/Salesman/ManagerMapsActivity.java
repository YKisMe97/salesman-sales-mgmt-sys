package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;


public class ManagerMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    double end_latitude,end_longitude;
    LatLng dalatlng;
    Marker mcurrMarker;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_maps);
        Toolbar toolbar= (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Salesman Location");

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        Intent intentVisitPlan = getIntent();
        List<String> string_lat = intentVisitPlan.getStringArrayListExtra("lat");
        List<String> stringlong = intentVisitPlan.getStringArrayListExtra("long");
        for (int i=0,j=0;i<string_lat.size()&&j<stringlong.size();i++,j++){
          Double dalat= Double.parseDouble(string_lat.get(i));
            Double dalong=  Double.parseDouble(stringlong.get(i));
            dalatlng = new LatLng(dalat,dalong);

           MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(dalatlng);
            markerOptions.draggable(true);
            markerOptions.title("");
            String snippet = "Customer Details\n";
            markerOptions.snippet(snippet);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(dalatlng));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(8));
            mMap.addMarker(markerOptions);
        }

        //Toast.makeText(MapsActivity.this,"Document found ",Toast.LENGTH_LONG).show();
                /* getNearbyPlacesData.execute(dataTransfer);*/

    }
    @Override
    public void onLocationChanged(Location location) {

        mMap.moveCamera(CameraUpdateFactory.newLatLng(dalatlng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
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
