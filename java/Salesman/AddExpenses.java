package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.SalesManager.ClaimList;
import com.example.priyanka.mapsnearbyplaces.R;

public class AddExpenses extends AppCompatActivity {
    private EditText expense;
    private double distance = 200.00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);
        expense = findViewById(R.id.expense);
        Toolbar toolbar= (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Add Expenses");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Button visitRoute = findViewById(R.id.visitRoute);
        Button addExpenses = findViewById(R.id.addExpenses);
        visitRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddExpenses.this,VisitPlan.class);
                startActivity(intent);
            }
        });

        addExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddExpenses.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddExpenses.this, ClaimList.class);
                startActivity(intent);
            }
        });

        expense.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double total_expense = distance * 0.60;
                String string_total_expense = Double.toString(total_expense);
                    expense.setText("100.00");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

}
