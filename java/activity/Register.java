package com.example.priyanka.mapsnearbyplaces.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText username,useremail,userpassword,user_confirm_password,user_phoneNum;
    private TextView loginPage;
    private Button button_register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginPage = findViewById(R.id.loginPage);
        mAuth = FirebaseAuth.getInstance();
        Register();
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

    }

    public void Register(){

        username = findViewById(R.id.reg_username);
        userpassword= findViewById(R.id.reg_pass);
        useremail = findViewById(R.id.reg_email);
        user_confirm_password=findViewById(R.id.reg_confirm_pass);
        user_phoneNum=findViewById(R.id.reg_phoneNum);

        button_register = (Button)findViewById(R.id.button_register);
        button_register.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {

                        Intent intent = new Intent(Register.this,MenuActivity.class);
                        String name = username.getText().toString().trim();
                        String email = useremail.getText().toString().trim();
                        String password = userpassword.getText().toString().trim();
                        String confirm_password = user_confirm_password.getText().toString().trim();
                        String phoneNum = user_phoneNum.getText().toString().trim();
                        registerAuthentication(name,phoneNum,email, password);
                        if (!password.equals(confirm_password)){
                            Toast.makeText(Register.this,"password and confirm password not same!",Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

    }
    private void registerAuthentication(final String name, final String phonenumber, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userId= firebaseUser.getUid();
                            Map<String, Object> user= new HashMap<>();
                            user.put("username",name);
                            user.put("userId", userId);
                            user.put("phoneNum",phonenumber);
                            db.collection("user")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            Toast.makeText(Register.this,"Added Successfully!",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Log.w(TAG, "Error adding document", e);
                                            Toast.makeText(Register.this,"Sum Ting Wong!",Toast.LENGTH_SHORT).show();

                                        }
                                    });

                            Toast.makeText(Register.this, "Authentication succeed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


}
