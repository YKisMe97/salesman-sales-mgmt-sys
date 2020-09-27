package com.example.priyanka.mapsnearbyplaces.SalesManager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Adapter.ClaimListAdapter;
import com.example.priyanka.mapsnearbyplaces.Adapter.SalesmanAdapter;
import com.example.priyanka.mapsnearbyplaces.Model.Salesmen;
import com.example.priyanka.mapsnearbyplaces.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ClaimList extends AppCompatActivity {

    private ClaimListAdapter claimListAdapter;
    private SalesmanAdapter salesmanAdapter;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private String currrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String current_companyId;
    private String documentPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_list);
        Toolbar toolbar= (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Claim List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setUpRecycleView();

    }
    private void setUpRecycleView() {
        db.collection("company_user").whereEqualTo("userId",currrentUser).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                if(querySnapshot!=null && !querySnapshot.isEmpty()){
                    for (QueryDocumentSnapshot queryDocumentSnapshot: querySnapshot){
                        current_companyId = queryDocumentSnapshot.getString("companyId");
                    }
                    }
            }
        });

        Query q = db.collection("company_user").whereEqualTo("companyId",current_companyId)
                .whereEqualTo("userCategory","Salesman");
        FirestoreRecyclerOptions<Salesmen> options = new FirestoreRecyclerOptions.Builder<Salesmen>().setQuery(q,Salesmen.class).build();
        salesmanAdapter =new SalesmanAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.ClaimListRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(salesmanAdapter);

        Toast.makeText(ClaimList.this, current_companyId, Toast.LENGTH_SHORT).show();
     /*   claimListAdapter.setOnItemClickListener(new ClaimListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Expenses customerInfo = documentSnapshot.toObject(Expenses.class);
                String custName1 = documentSnapshot.getId();
                String geoPoint = documentSnapshot.getGeoPoint("geoPoint").toString();
                String corporate = documentSnapshot.getString("corporate");
                String custName = documentSnapshot.getString("custName");
                String path =documentSnapshot.getReference().getPath();
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
