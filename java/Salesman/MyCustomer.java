package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Adapter.CustomerAdapter;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.util.FileManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;

public class MyCustomer extends AppCompatActivity{
private CustomerAdapter customerAdapter;
private RecyclerView recyclerView;
private ArrayList<String> customerList;
private FirebaseFirestore db =FirebaseFirestore.getInstance();
private String currrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_customer);

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("My Customer");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setUpRecycleView();

    }


    private void setUpRecycleView() {
        //Query q = db.collection("cust_place").whereEqualTo("userId", currrentUser);
        //FirestoreRecyclerOptions<CustomerInfo> options = new FirestoreRecyclerOptions.Builder<CustomerInfo>().setQuery(q, CustomerInfo.class).build();

        String path = FileManager.getPublicDirPath(MyCustomer.this) + File.separator + "My Customer";
        File myCustomerDirectory = new File(path);
        if (!myCustomerDirectory.exists()){
            myCustomerDirectory.mkdirs();
        }
        File[] customerFiles = myCustomerDirectory.listFiles();
        int total_customer_files = customerFiles.length;
        customerList= new ArrayList<>();
        for (int i = 0; i<total_customer_files;i++){
            customerList.add(customerFiles[i].getName());
        }

        customerAdapter = new CustomerAdapter(customerList, MyCustomer.this);
        recyclerView = findViewById(R.id.customerRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customerAdapter);
        new ItemTouchHelper(itemTouchCallBackHelper).attachToRecyclerView(recyclerView);
    }
    ItemTouchHelper.SimpleCallback itemTouchCallBackHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            AlertDialog.Builder alert_no_order_file = new AlertDialog.Builder(MyCustomer.this);
            alert_no_order_file.setTitle("Delete Customer");
            alert_no_order_file.setMessage(getString(R.string.delete_question));
            alert_no_order_file.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //FileManager.removeCustFile(MyCustomer.this, customerList.get(position));
                    customerAdapter.notifyDataSetChanged();
                    Toast.makeText(MyCustomer.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                }
            });
            alert_no_order_file.setNegativeButton("No", null);
            alert_no_order_file.show();
        }
    };
        /*customerAdapter.setOnItemClickListener(new CustomerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                CustomerInfo customerInfo = documentSnapshot.toObject(CustomerInfo.class);
                String custName1 = documentSnapshot.getId();
                String geoPoint = documentSnapshot.getGeoPoint("geoPoint").toString();
                String corporate = documentSnapshot.getString("corporate");
                String custName = documentSnapshot.getString("custName");
                String path =documentSnapshot.getReference().getPath();
                Intent intent = new Intent(MyCustomer.this, CustomerDetail.class);
                intent.putExtra("corporate",corporate);
                intent.putExtra("custName",custName);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        customerAdapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        customerAdapter.stopListening();
    }
    */

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
            Intent intent = new Intent(MyCustomer.this, AddCustomer.class);
            startActivity(intent);
        }
        return true;
    }
}
