package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Model.Company;
import com.example.priyanka.mapsnearbyplaces.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class GraphActivity extends AppCompatActivity {
FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String TAG="ERROR TAG:";
    BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
       /* Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Graph");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        barChart= (BarChart)findViewById(R.id.barChart);
        //Monthly Sales: Shows All Same Company Salesman Total Order Sales
        //each bar = one customer order all document price sum up
        //total bar are salesman who contain same companyID with manager
     /*   db.collection("company_user").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        doc.getDocument().getReference().collection("order").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.d("", "Error : " + e.getMessage());
                                }for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        String totalOrder = doc.getDocument().getString("totalPrice");


                                    }
                                    }
                            }
                        });
                    }
            }
                }
        });*/
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30.00f));
        entries.add(new BarEntry(1f, 130.10f));
        entries.add(new BarEntry(2f, 250f));
        entries.add(new BarEntry(3f, 530f));
        entries.add(new BarEntry(4f, 430f));
                 //  }
                    BarDataSet set = new BarDataSet(entries, "Salesman");

                    BarData data = new BarData(set);
                    data.setBarWidth(0.9f); // set custom bar width
                    barChart.setData(data);
                    barChart.setFitBars(true); // make the x-axis fit exactly all bars
                    barChart.invalidate(); // refresh*/
                    //Toast.makeText(GraphActivity.this,user_size,Toast.LENGTH_LONG).show();


               /* }else
                {
                    Toast.makeText(GraphActivity.this,"ERROR",Toast.LENGTH_LONG).show();
                }*/
            }
      //  });



    //}
}
