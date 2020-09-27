package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.priyanka.mapsnearbyplaces.R;

public class AddVisitPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit_plan);
        Toolbar toolbar= (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Add Visit Plan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
