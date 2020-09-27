package com.example.priyanka.mapsnearbyplaces.activity;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.priyanka.mapsnearbyplaces.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfile extends AppCompatActivity {
    private TextView user_name, phoneNo, my_email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private CollectionReference profileCollectionRef = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("My Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        phoneNo = (TextView) findViewById(R.id.phoneNum);
        my_email = (TextView) findViewById(R.id.email);
        user_name = (TextView) findViewById(R.id.username);
        String current_userId = mAuth.getCurrentUser().getUid();

        String email = mAuth.getCurrentUser().getEmail();
        my_email.setText(email);
       /* Query userId_query= profileCollectionRef.whereEqualTo("userId",current_userId);
        userId_query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            String username = documentSnapshot.getString("username");
                            Integer phoneNum = (Integer) documentSnapshot.get("phoneNum");

                            user_name.setText(username);
                            phoneNo.setText(phoneNum);

                        }
                    }else{
                       Toast.makeText(MyProfile.this,"Sum Thing Wong",Toast.LENGTH_LONG).show();
                    }

                }
            });*/
    }
}