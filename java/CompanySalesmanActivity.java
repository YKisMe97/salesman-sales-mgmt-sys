package com.example.priyanka.mapsnearbyplaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Adapter.SalesmanAdapter;
import com.example.priyanka.mapsnearbyplaces.Model.Salesmen;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CompanySalesmanActivity extends AppCompatActivity {
    private SalesmanAdapter salesmanAdapter;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private String currrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String companyId = "ibumie01";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_salesman);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Company's Salesman");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpRecyclerView();
    }
    public void setUpRecyclerView(){
           //
        Toast.makeText(this, companyId, Toast.LENGTH_SHORT).show();
            Query q = db.collection("company_user").whereEqualTo("userCategory", "Salesman").whereEqualTo("companyId", companyId);
            FirestoreRecyclerOptions<Salesmen> options = new FirestoreRecyclerOptions.Builder<Salesmen>().setQuery(q, Salesmen.class).build();
            salesmanAdapter = new SalesmanAdapter(options);
            RecyclerView recyclerView = findViewById(R.id.salesmenRecycleView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(CompanySalesmanActivity.this));
            recyclerView.setAdapter(salesmanAdapter);


       /* salesmanAdapter.setOnItemClickListener(new SalesmanAdapter.OnItemClickListener(){
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Salesmen salesmen = documentSnapshot.get(Salesmen.class);
            }

        });*/
    }


    @Override
    public void onStart(){
        super.onStart();
        salesmanAdapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        salesmanAdapter.stopListening();
    }

}