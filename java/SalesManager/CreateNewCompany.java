package com.example.priyanka.mapsnearbyplaces.SalesManager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.activity.MenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNewCompany extends AppCompatActivity {
private FirebaseFirestore db = FirebaseFirestore.getInstance();
private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_company);

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Create New");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CreateCompany();
    }

    public void CreateCompany() {
        final EditText companyId = findViewById(R.id.companyId);
        final EditText companyName = findViewById(R.id.companyName);
        final EditText passcode = findViewById(R.id.passcode);
        final EditText passcode_confirm = findViewById(R.id.passcode_confirm);
        final EditText companyType = findViewById(R.id.companyType);
                Button submit = findViewById(R.id.btn_createCompany);

        final String addPasscode = passcode.getText().toString();
        String Passcode_confirm_string = passcode_confirm.getText().toString();
        if (addPasscode.equals(Passcode_confirm_string)){
            submit.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String addCompanyId = companyId.getText().toString();
                            String addCompanyName = companyName.getText().toString();
                            String addCompanyType = companyType.getText().toString();

                            Map<String, Object> company = new HashMap<>();
                            company.put("companyId", addCompanyId);
                            company.put("companyName", addCompanyName);
                            company.put("companyType", addCompanyName);
                            company.put("passcode", addPasscode);

                            Map<String, Object> company_user = new HashMap<>();
                            company_user.put("userId",currentUser);
                            company_user.put("userCategory", "Manager");
                            company_user.put("companyId",addCompanyId);
                            db.collection("company").document(addCompanyId).set(company);
                            Toast.makeText(CreateNewCompany.this, "Succesfully created!", Toast.LENGTH_SHORT).show();

                            db.collection("company_user").document(currentUser).set(company_user);
                            Intent intent= new Intent(CreateNewCompany.this, MenuActivity.class);
                            startActivity(intent);
                        }
                    }
            );
         }else {
            Toast.makeText(CreateNewCompany.this, "Passcode and Confirm Passcode Not Matched!", Toast.LENGTH_LONG).show();
        }
    }
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.home_title){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/
}
