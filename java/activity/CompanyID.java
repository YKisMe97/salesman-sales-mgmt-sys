package com.example.priyanka.mapsnearbyplaces.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CompanyID extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private EditText companyId,passcode;
    private Button submit;
    private String string_companyId;
    private String string_passcode;
    private String companyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_id);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        companyId = findViewById(R.id.joinCompanyID);
        passcode = findViewById(R.id.joinPasscode);
        toolbar.setTitle("Join Company");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         submit = (Button)findViewById(R.id.join_submit);
         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 checkCompanyExisted();
             }
         });


    }

    public void checkCompanyExisted(){
        string_companyId = companyId.getText().toString();
        string_passcode = passcode.getText().toString();
        db.collection("company").whereEqualTo("companyId",string_companyId).whereEqualTo("passcode",string_passcode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot!=null && !querySnapshot.isEmpty()){
                        for (QueryDocumentSnapshot queryDocumentSnapshot: querySnapshot){
                             companyName = queryDocumentSnapshot.getString("companyName");
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyID.this);
                        builder.setCancelable(true);
                        builder.setTitle("Join Company");
                        builder.setMessage("The Company Name is "+ companyName +" , Confirm to join?");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        joinCompany();
                                        Toast.makeText(CompanyID.this, "Join Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CompanyID.this,MenuActivity.class);
                                        startActivity(intent);
                                    }
                                });

                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{

                        Toast.makeText(CompanyID.this, "Invalid Company ID or passcode", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public void joinCompany(){
        final Map<String, Object> joinCompany= new HashMap<>();
        joinCompany.put("userId",currentUser);
        joinCompany.put("userCategory", "Salesman");
        joinCompany.put("companyId", string_companyId);
        db.collection("company_user").document(currentUser).set(joinCompany);

    }
                    }
