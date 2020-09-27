package com.example.priyanka.mapsnearbyplaces.SalesManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Product;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.storage.sqlDatabase;
import com.example.priyanka.mapsnearbyplaces.util.FileManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {
private FirebaseFirestore db = FirebaseFirestore.getInstance();
private FirebaseAuth mAuth= FirebaseAuth.getInstance();
private Button btn_addProduct;
private EditText productName, productId, quantity,price;
private JSONObject productJsonObject;
private sqlDatabase mSQLDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Add Products");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_addProduct = findViewById(R.id.addProduct);
        productName =findViewById(R.id.addProductName);
        productId = findViewById(R.id.addProductId);
        price= findViewById(R.id.price);
        //quantity=findViewById(R.id.quantity);
        productJsonObject = new JSONObject();

        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(productName.getText())){
                    Toast.makeText(AddProduct.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(productId.getText())){
                    Toast.makeText(AddProduct.this, "Please Enter Product Id", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(price.getText())){
                    Toast.makeText(AddProduct.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                }else{

                    String string_productName= productName.getText().toString();
                    //int int_quantity =Integer.parseInt(quantity.getText().toString());
                    String string_productId = productId.getText().toString();
                    Double double_price =Double.parseDouble(price.getText().toString());

                    StringBuffer product_sb = new StringBuffer();
                    product_sb.append(string_productName + "^");
                    product_sb.append(double_price + "\r\n");

                    File product_file = new File(FileManager.getPrivateDirPath(AddProduct.this) + File.separator + "product" + File.separator + getString(R.string.company_products_file));
                    if (!product_file.exists()){
                        Toast.makeText(AddProduct.this, "Please download the product data!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mSQLDatabase = new sqlDatabase(AddProduct.this);
                        Cursor data = mSQLDatabase.getProductList();
                        ArrayList<String> products = new ArrayList<>();
                        StringBuilder all_product_sb = new StringBuilder();
                        while (data.moveToNext()){
                            int position = data.getPosition();
                            products.add(data.getString(data.getColumnIndex("productName")) + "^" +data.getString(data.getColumnIndex("productQuantity")));
                            all_product_sb.append(products.get(position)+ System.lineSeparator());
                            Log.d("FYP",products.get(position)+ System.lineSeparator());
                        }
                        all_product_sb.append(string_productName+ "^"+ double_price);
                        //FileManager.saveTempFile(AddProduct.this,"",getString(R.string.company_products_file),all_product_sb.toString());
                        FileManager.saveTempFile(AddProduct.this, File.separator + getString(R.string.product) + File.separator + getString(R.string.company_products_file), all_product_sb.toString());
                        mSQLDatabase.addNewProduct(string_productId,string_productName,double_price);
                        Toast.makeText(AddProduct.this, "Successfully Added New Product", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProduct.this, Product.class);
                        startActivity(intent);
                        finish();
                    }

                   /* Map<String,Object> product = new HashMap<>();
                    product.put("productName",string_productName);
                    product.put("quantity",int_quantity);
                    product.put("price",double_price);
                    db.collection("company").document("ibumie01").collection("product").add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddProduct.this, "Successfully Added New Product", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddProduct.this, Product.class);
                            startActivity(intent);
                        }
                    });*/
                }
            }
        });

    }

}
