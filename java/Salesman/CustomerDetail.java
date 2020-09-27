package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.priyanka.mapsnearbyplaces.R;

public class CustomerDetail extends AppCompatActivity {
private  String corporate,custName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        TextView textViewCorporate = findViewById(R.id.textViewCorporate);
        TextView textViewCustName = findViewById(R.id.textViewCustName);
        Bundle extras = getIntent().getExtras();
        //corporate= extras.getString("corporate");
        custName=extras.getString("custName");
        //textViewCorporate.setText(corporate);
        textViewCustName.setText(custName);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle(custName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
