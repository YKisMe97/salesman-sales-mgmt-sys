package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.util.FileManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCustomer extends AppCompatActivity {
    private LatLng custLocation;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUser = mAuth.getCurrentUser().getUid();
    EditText custName, corporate,address,phoneNum;
    Button addCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        custName = findViewById(R.id.custName);
        corporate = findViewById(R.id.corporate);
        address = findViewById(R.id.address);
        phoneNum = findViewById(R.id.phoneNum);
        addCustomer = findViewById(R.id.buttonAddCustomer);
        Toolbar toolbar= findViewById(R.id.toolbar1);
        toolbar.setTitle("Add Customer");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(custName.getText())){
                    Toast.makeText(AddCustomer.this, "Please Enter Customer Name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(corporate.getText())){
                    Toast.makeText(AddCustomer.this, "Please Enter Corporate", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(address.getText())){
                    Toast.makeText(AddCustomer.this, "Address is Empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phoneNum.getText())){
                    Toast.makeText(AddCustomer.this, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
                }else{
                    String string_custName = custName.getText().toString();
                    String string_corporate = corporate.getText().toString();
                    String string_address = address.getText().toString();
                    String string_phone = phoneNum.getText().toString();
                    //set all information into a string buffer
                    StringBuilder sb_cust = new StringBuilder();
                    sb_cust.append(string_custName + System.lineSeparator());
                    sb_cust.append(string_corporate + System.lineSeparator());
                    sb_cust.append(string_address + System.lineSeparator());
                    sb_cust.append(string_phone + System.lineSeparator());
                    String data = sb_cust.toString();
                    //save to File
                    FileManager.saveFile(AddCustomer.this,"my_customer",string_custName+".txt",data);
                    Toast.makeText(AddCustomer.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCustomer.this, MyCustomer.class);
                    startActivity(intent);
                    finish();
                    /*Map<String,Object> customer = new HashMap<>();
                    customer.put("custName",string_custName);
                    customer.put("corporate",string_corporate);
                    customer.put("address",string_address);
                    customer.put("phone",string_phone);
                    customer.put("userId",currentUser);
                    db.collection("cust_place").add(customer).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddCustomer.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddCustomer.this, MyCustomer.class);
                            startActivity(intent);
                        }
                    });*/
                }
            }
        });
    }
}
