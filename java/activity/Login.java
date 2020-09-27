package com.example.priyanka.mapsnearbyplaces.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private static EditText usernameField;
    private static EditText passwordField;
    private static TextView attempt;
    private static Button mLoginbtn;
    private static TextView register_btn;
    int attempt_counter = 5;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mLoginbtn=(Button) findViewById(R.id.login_button);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!= null){
                    /* Toast.makeText(Login.this, "Welcome Back ",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MenuActivity.class
                    );
                    startActivity(intent);*/
                    verifyUserCat();
                }
            }
        };
            LoginButton();
            button_register();
        }
        protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void LoginButton() {
        usernameField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        mLoginbtn = (Button) findViewById(R.id.login_button);

        mLoginbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startLogin();
                      /*  if(username.getText().toString().equals("user")|| password.getText().toString().equals("pass")){
                            Toast.makeText(Login.this, "Welcome Back, "+username,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MenuActivity.class
                            );
                            startActivity(intent);

                        }

                        else{
                            Toast.makeText(Login.this, "Username or password is NOT correct",Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            attempt.setText(Integer.toString(attempt_counter));
                            if (attempt_counter == 0)
                                login_button.setEnabled(false);
                        }*/
                    }
                }
        );}



        private void startLogin(){
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                Toast.makeText(Login.this, "Please fill in the blank", Toast.LENGTH_LONG).show();
            }else {
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verifyUserCat();

                        }else{
                            Toast.makeText(Login.this, "Wrong username or password or No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        }

        public void button_register(){

            register_btn=(TextView) findViewById(R.id.textView_register);
            register_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                }
            });
        }
        public void verifyUserCat(){
            String currentuser = mAuth.getCurrentUser().getUid();
            db.collection("company_user").document(currentuser).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot != null && documentSnapshot.exists()) {
                                    Intent intent = new Intent(Login.this, MenuActivity.class);
                                    startActivity(intent);
                                }else{

                                    Intent intent2 = new Intent(Login.this,HomeActivity.class);
                                    startActivity(intent2);
                                }

                            }
                        }
                    });
        }
}