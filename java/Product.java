package com.example.priyanka.mapsnearbyplaces;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Adapter.ProductAdapter;
import com.example.priyanka.mapsnearbyplaces.Model.Company;
import com.example.priyanka.mapsnearbyplaces.Model.ProductInfo;
import com.example.priyanka.mapsnearbyplaces.SalesManager.AddProduct;
import com.example.priyanka.mapsnearbyplaces.storage.sqlDatabase;
import com.example.priyanka.mapsnearbyplaces.util.FTPManager;
import com.example.priyanka.mapsnearbyplaces.util.FileManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Product extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private String currrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private sqlDatabase mSqlDatabase;
    private FloatingActionButton fab_upload_product;
    JSONObject jsonCompanyId = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fab_upload_product = findViewById(R.id.fab_upload_product);

        mSqlDatabase = new sqlDatabase(this);
        setUpRecycleView();

        fab_upload_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   // FTPClient ftpClient = new FTPClient();
                    AlertDialog.Builder alert_no_order_file = new AlertDialog.Builder(Product.this);
                    alert_no_order_file.setTitle("Send Order");
                    alert_no_order_file.setMessage("Are you sure want to send now?");
                    alert_no_order_file.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FTPManager ftpManager = new FTPManager();
                            String localFilePath = FileManager.getPrivateDirPath(Product.this) + File.separator + getString(R.string.product) + File.separator + getString(R.string.company_products_file);
                            String RemoteDirPath = "/KUAN/product/";
                            //ftpClient.setConnectTimeout(10 * 1000);
                            ftpManager.uploadAllToServer(Product.this,"infocomm.homelinux.com","nyk","nyk",8899, RemoteDirPath);
                            Toast.makeText(Product.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert_no_order_file.setNegativeButton("No", null);
                    alert_no_order_file.show();

                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("FYP",e.toString());
                }
            }
        });
    }
    private void setUpRecycleView() {
        ArrayList<String> products = new ArrayList<>();
        Cursor data = mSqlDatabase.getProductList();
        productAdapter =new ProductAdapter(this, data);
        RecyclerView recyclerView = findViewById(R.id.productRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
        if(data.getCount()==0){
            Toast.makeText(this, "No product existed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }
    public interface MyCallback{
        void onCallback(String companyId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Product.this, AddProduct.class);
            startActivity(intent);
        }
        return true;
    }
    private void getCompanyId(final MyCallback myCallback) {
        DocumentReference docRef=  db.collection("company_user").document(currrentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document =task.getResult();
                    Company company=document.toObject(Company.class);
                    String companyId = company.getCompanyId();
                    try {
                        jsonCompanyId.put("companyId",companyId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myCallback.onCallback(companyId);
                }
                else{
                    Toast.makeText(Product.this, "ERROR!", Toast.LENGTH_SHORT).show();
                }
                //get salesman which same companyID with manager
            }

        });
    }


}
