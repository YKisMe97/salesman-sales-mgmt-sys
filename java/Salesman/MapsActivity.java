package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Adapter.CustomInfoWindowAdapter;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.util.GetDirectionsData;
import com.example.priyanka.mapsnearbyplaces.util.GetNearbyPlacesData;
import com.example.priyanka.mapsnearbyplaces.util.PlaceAutocompleteAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener
        {

    private GoogleMap mMap;
    @ServerTimestamp Date time;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude,lat,lng;
    LatLng latlng,latLng;
    double end_latitude, end_longitude;
    LatLng PlaceLatLng;
    String PlaceName;
    Button btn_takeAttendance, btn_VisitPlan;
    Polyline cPolyline;
    PlacesClient placesClient;
    String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collRef = db.collection("cust_place");
            //String placeselected;
            private AutoCompleteTextView mSearchText;
            private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
           // private static final LatLngBounds latlngbounds=new LatLngBounds(new LatLng(0.7896676,100.5086745),new LatLng(7.534965,119.103498));
            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
                btn_takeAttendance = findViewById(R.id.btn_takeAttendance);
                btn_VisitPlan = findViewById(R.id.addVisit);
                String PlacesAPIkey= "AIzaSyCsVdFlEV30VUX7GsEbPbG-kXayYF_I8io";
                if(!Places.isInitialized()){
                    Places.initialize(getApplicationContext(),PlacesAPIkey);
                }
                placesClient = Places.createClient(this);
                final AutocompleteSupportFragment ASP =(AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
                ASP.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
                ASP.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(@NonNull Place place) {
                       PlaceLatLng = place.getLatLng();
                        PlaceName = place.getName();
                    }

                    @Override
                    public void onError(@NonNull Status status) {
                        Toast.makeText(MapsActivity.this, "On Error", Toast.LENGTH_SHORT).show();
                    }
                });
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_takeAttendance.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Map<String,Object> attendance = new HashMap<>();
        GeoPoint currentGP =new GeoPoint(latitude,longitude);
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        attendance.put("location",currentGP);
        attendance.put("datetime",FieldValue.serverTimestamp());
        db.collection("company_user/"+currentUser+"/attendance").add(attendance)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        btn_takeAttendance.setEnabled(false);
                        Toast.makeText(MapsActivity.this, "Successfully take attendance", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Log.w(TAG, "Error adding document", e);
                Toast.makeText(MapsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
});
        btn_VisitPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> visitPlan = new HashMap<>();
                GeoPoint currentGP =new GeoPoint(latitude,longitude);
                GeoPoint visitPlanLocation = new GeoPoint(end_latitude,end_longitude);
                List<GeoPoint> visitPlanRoute = Arrays.asList(currentGP,visitPlanLocation);
                visitPlan.put("visitPlanRoute",visitPlanRoute);
                db.collection("company_user/"+currentUser+"/visitPlan").add(visitPlan)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MapsActivity.this, "Successfully add to Visit Plan", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error adding document", e);
                        Toast.makeText(MapsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

      //  mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,latlngbounds,null);
      //  mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        isProviderEnabled();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    public void onClick(View v)
    {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        switch(v.getId()) {
            case R.id.B_search: {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(PlaceLatLng);
                markerOptions.title(PlaceName);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(PlaceLatLng));
                     }
            break;
            case R.id.B_nearby:
                mMap.clear();
               String customer = "customer";
                String url = getUrl(lat, lng, customer);
                collRef.whereEqualTo("userId",currentUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        // Document found in the offline cache
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        GeoPoint gp=documentSnapshot.getGeoPoint("geoPoint");
                            String custName =documentSnapshot.getString("custName");
                            String corporate =documentSnapshot.getString("corporate");
                            String snippet ="Customer Details\n" +"Customer Name: "+ custName +"\n"+
                                    "Corporate: "+ corporate + "\n";

                        lat= gp.getLatitude();
                        lng=gp.getLongitude();
                        latlng =new LatLng(lat,lng);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latlng);
                        markerOptions.draggable(true);
                        markerOptions.title("");
                        markerOptions.snippet(snippet);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                        markerOptions.visible(false);
                            mCurrLocationMarker = mMap.addMarker(markerOptions);
                            if(SphericalUtil.computeDistanceBetween(latLng,mCurrLocationMarker.getPosition())<5000){
                            mCurrLocationMarker.setVisible(true);
                        }
                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                        //Toast.makeText(MapsActivity.this,"Document found ",Toast.LENGTH_LONG).show();
                    }

                }
        });

                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));

               /* dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);*/
                Toast.makeText(MapsActivity.this, "Showing Nearby Customers", Toast.LENGTH_LONG).show();
                break;

            case R.id.B_showall:
                mMap.clear();
                dataTransfer = new Object[2];
                String nearby = "nearby";
                url = getUrl(latitude, longitude, nearby);
                getNearbyPlacesData = new GetNearbyPlacesData();
               dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                collRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                    // Document found in the offline cache
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        GeoPoint gp=documentSnapshot.getGeoPoint("geoPoint");
                        String custName =documentSnapshot.getString("custName");
                        String corporate =documentSnapshot.getString("corporate");
                        String snippet ="Customer Details\n" +"Customer Name: "+ custName +"\n"+
                                "Corporate: "+ corporate + "\n";


                        lat= gp.getLatitude();
                        lng=gp.getLongitude();
                        latlng =new LatLng(lat,lng);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latlng);
                        markerOptions.draggable(true);
                        markerOptions.title("");
                        markerOptions.snippet(snippet);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        mCurrLocationMarker = mMap.addMarker(markerOptions);
                        //Toast.makeText(MapsActivity.this,"Document found ",Toast.LENGTH_LONG).show();
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));

                    }

                }
            });
               /* getNearbyPlacesData.execute(dataTransfer);*/
                Toast.makeText(MapsActivity.this, "Show All Customers", Toast.LENGTH_LONG).show();
                break;

            case R.id.B_clear:
                mMap.clear();
                String school = "school";
                dataTransfer = new Object[2];
                url = getUrl(latitude, longitude, school);
                getNearbyPlacesData = new GetNearbyPlacesData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                //getNearbyPlacesData.execute(dataTransfer);

                break;

            case R.id.B_to:
                dataTransfer = new Object[3];
                url = getDirectionsUrl();
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                getDirectionsData.execute(dataTransfer);
                Location locationA = new Location("A");
                locationA.setLatitude(latitude);
                locationA.setLongitude(longitude);
                Location locationB =new Location("B");
                locationB.setLatitude(end_latitude);
                locationB.setLongitude(end_longitude);
                float distance = locationA.distanceTo(locationB);
                float distance_km= distance/1000; //convert from m to km
                Toast.makeText(MapsActivity.this, "Directing To the location, Distance is "+ distance_km+ " km", Toast.LENGTH_LONG).show();
                break;


        }
    }

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyCsVdFlEV30VUX7GsEbPbG-kXayYF_I8io");

        return googleDirectionsUrl.toString();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCsVdFlEV30VUX7GsEbPbG-kXayYF_I8io");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }




    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStop(){
        super.onStop();

    }

    public void isProviderEnabled(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        Toast.makeText(MapsActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.setDraggable(true);
                end_latitude = marker.getPosition().latitude;
                end_longitude =  marker.getPosition().longitude;

                Log.d("end_lat",""+end_latitude);
                Log.d("end_lng",""+end_longitude);
                return false;
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                end_latitude = marker.getPosition().latitude;
                end_longitude =  marker.getPosition().longitude;

                Log.d("end_lat",""+end_latitude);
                Log.d("end_lng",""+end_longitude);
            }


        }


