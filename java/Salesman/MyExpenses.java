package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.priyanka.mapsnearbyplaces.Adapter.ExpenseAdapter;
import com.example.priyanka.mapsnearbyplaces.Model.Expenses;
import com.example.priyanka.mapsnearbyplaces.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyExpenses extends AppCompatActivity {

    private ExpenseAdapter expenseAdapter;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private String currrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expenses);
        Toolbar toolbar= (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("My Expenses");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setUpRecycleView();
    }

    private void setUpRecycleView() {
        Query q = db.collection("company_user").document(currrentUser).collection("visitPlan").document().collection("expense");
        FirestoreRecyclerOptions<Expenses> options = new FirestoreRecyclerOptions.Builder<Expenses>().setQuery(q,Expenses.class).build();
        expenseAdapter = new ExpenseAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.myExpensesRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);
    }
    @Override
    public void onStart(){
        super.onStart();
        expenseAdapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        expenseAdapter.stopListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MyExpenses.this, AddExpenses.class);
            startActivity(intent);
        }
        return true;
    }
}
