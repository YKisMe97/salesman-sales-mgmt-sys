package com.example.priyanka.mapsnearbyplaces.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.SalesManager.CreateNewCompany;
import com.example.priyanka.mapsnearbyplaces.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button joinCompany = findViewById(R.id.joinCompany);
        Button createCompany = findViewById(R.id.createCompany);
        Button signOut = findViewById(R.id.homeSignOut);

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        joinCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CompanyID.class);
                startActivity(intent);
            }
        });

        createCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CreateNewCompany.class);
                startActivity(intent);
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Title");
                builder.setMessage("Are you want to logout?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null){
                                    mAuth.signOut();
                                    Toast.makeText(HomeActivity.this, user.getEmail()+ " Sign out!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(HomeActivity.this, Login.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(HomeActivity.this, "You aren't login Yet!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

         }
}
