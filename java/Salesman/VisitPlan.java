package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.priyanka.mapsnearbyplaces.Adapter.VisitPlanAdapter;
import com.example.priyanka.mapsnearbyplaces.Model.VisitPlanInfo;
import com.example.priyanka.mapsnearbyplaces.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class VisitPlan extends AppCompatActivity{
    private VisitPlanAdapter visitPlanAdapter;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_plan);

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Visit Plan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setUpRecycleView();

    }

    private void setUpRecycleView() {
        Query q =  db.collection("company_user").document(currentUser).collection("visitPlan");
        FirestoreRecyclerOptions<VisitPlanInfo> options = new FirestoreRecyclerOptions.Builder<VisitPlanInfo>().setQuery(q,VisitPlanInfo.class).build();
        visitPlanAdapter =new VisitPlanAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.visitPlanRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(visitPlanAdapter);

        visitPlanAdapter.setOnItemClickListener(new VisitPlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                VisitPlanInfo visitPlanInfo = documentSnapshot.toObject(VisitPlanInfo.class);
                ArrayList<GeoPoint> route = visitPlanInfo.getVisitPlanRoute();
                ArrayList<String> string_lat = new ArrayList<>();
                ArrayList<String> string_long = new ArrayList<>();
                for (int i=0;i<route.size();i++) {
                    String ggpplat = Double.toString(route.get(i).getLatitude());
                    String ggpplong = Double.toString(route.get(i).getLongitude());
                    string_lat.add(ggpplat);
                    string_long.add(ggpplong);
                }
                //String custName = documentSnapshot.getString("custName");
                //String path =documentSnapshot.getReference().getPath();
                Intent intent = new Intent(VisitPlan.this, ManagerMapsActivity.class);
                intent.putStringArrayListExtra("lat",string_lat);
                intent.putStringArrayListExtra("long",string_long);
                startActivity(intent);

            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        visitPlanAdapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        visitPlanAdapter.stopListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(VisitPlan.this, MapsActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
