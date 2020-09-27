package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.activity.MenuActivity;
import com.example.priyanka.mapsnearbyplaces.storage.SharedPreferenceStorage;
import com.example.priyanka.mapsnearbyplaces.util.FileManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class AddOrder extends AppCompatActivity implements TextWatcher {
    private Spinner spinner_product, spinner_customer;
    private String[] productArray, productPriceArray, customerArray;
    private ArrayList<String> orderArrayList;
    private EditText productQuantity, productPrice, totalPrice;
    private Button addNewOrder;
    private RecyclerView order_recylerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Add Order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        productQuantity = findViewById(R.id.editText_quantity);
        productPrice = findViewById(R.id.editText_product_price);
        spinner_product = findViewById(R.id.spinner_product);
        spinner_customer = findViewById(R.id.spinner_myCustomer);
        addNewOrder = findViewById(R.id.add_new_order);
        totalPrice = findViewById(R.id.total_price);
        order_recylerView = findViewById(R.id.order_recylerView);
        orderArrayList = new ArrayList<>();
        getProductList();
        getCustomerList();
        productQuantity.addTextChangedListener(this);
        productPrice.addTextChangedListener(this);

        addNewOrder();
    }

    private void getProductList(){
        productArray = new String[]{""};
        String product_filePath = FileManager.getPrivateDirPath(this) + File.separator + "product" + File.separator + getString(R.string.company_products_file);
        final String[] products = FileManager.retrieveTextContentAsStringArray(this, product_filePath);
        int productList_length =products.length;
        productPriceArray = new String[productList_length+1];
        if(products!=null) {
            productArray = new String[productList_length + 1];
            productArray[0] = "- Please Select Product -";
            productPriceArray[0]="0.0";
            //System.arraycopy(products, 0, productArray, 1, products.length);
            for (int i = 1; i<products.length+1;i++) {
               String[] split_product_price = products[i-1].split("\\^",2);
                String product_name = split_product_price[0];
                productPriceArray[i] = split_product_price[1];
                productArray[i] = product_name;
                productPrice.setText("0.0");
            }

        }
        else {
            AlertDialog.Builder alert_no_product_file = new AlertDialog.Builder(this);
            alert_no_product_file.setTitle("Product Not Found");
            alert_no_product_file.setMessage("Please Download Product Details From Server!");
            alert_no_product_file.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(AddOrder.this, MenuActivity.class);
                    startActivity(intent);
                }
            });
            alert_no_product_file.show();
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_product.setAdapter(spinnerArrayAdapter);
        File product_file = new File(FileManager.getPrivateDirPath(AddOrder.this) + File.separator + getString(R.string.product) + File.separator + getString(R.string.company_products_file));
        if (product_file.exists()) {
            Toast.makeText(this, "File existed", Toast.LENGTH_SHORT).show();
        }
        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                productPrice.setText(productPriceArray[position]);
                //Toast.makeText(AddOrder.this, "Test", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCustomerList(){
        customerArray = new String[]{""};
        String path = FileManager.getPublicDirPath(AddOrder.this) + File.separator + "My Customer";
        File myCustomerDirectory = new File(path);
        if (!myCustomerDirectory.exists()){
            //myCustomerDirectory.mkdirs();
            Toast.makeText(this, "No customer List", Toast.LENGTH_SHORT).show();
            Log.d("FYP", "No customer list or error");
        }
        File[] customerFiles = myCustomerDirectory.listFiles();
        int total_customer_files = customerFiles.length;
        Log.d("FYP","Total Customers: "+total_customer_files);
        //customerArray[0]="";
        if (total_customer_files > 0) {
            customerArray = new String[total_customer_files];
            try{
                for (int i = 0; i < total_customer_files; i++) {
                    customerArray[i] = customerFiles[i].getName().replace(".txt","");
                }
                ArrayAdapter<String> spinnerCustomerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, customerArray);
                spinnerCustomerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_customer.setAdapter(spinnerCustomerAdapter);
            }
            catch (Exception e){
                Log.d("FYP","Error "+e);
            }

        }
        else{
            Toast.makeText(this, "Customer List got probom", Toast.LENGTH_SHORT).show();

        }
    }

    private void addNewOrder(){
        addNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName =  productArray[spinner_product.getSelectedItemPosition()];
                Double getProductPrice = Double.parseDouble(productPrice.getText().toString());
                int getProductQuantity= Integer.parseInt(productQuantity.getText().toString());
                Double totalPrice = getProductPrice * getProductQuantity;

                if (TextUtils.isEmpty(productQuantity.getText())){
                    Toast.makeText(AddOrder.this, "Please insert product quantity", Toast.LENGTH_SHORT).show();
                }
                else if (spinner_product.getSelectedItemPosition()==0){
                    Toast.makeText(AddOrder.this, "Select a product", Toast.LENGTH_SHORT).show();
                }
                else {
                    StringBuilder order_sb = new StringBuilder();
                    order_sb.append(productName+ "^");
                    order_sb.append(getProductQuantity+ "^");
                    order_sb.append(totalPrice+ "^");
                    //order_sb.append(System.lineSeparator());
                    orderArrayList.add(order_sb.toString());
                    //save to temp file first
                    StringBuilder final_order_sb = new StringBuilder();
                    for (int i=0;i< orderArrayList.size();i++){
                        final_order_sb.append(orderArrayList.get(i));
                        final_order_sb.append(System.lineSeparator());
                    }
                    FileManager.saveTempFile(AddOrder.this, getString(R.string.order_temp_file), final_order_sb.toString());
                    Toast.makeText(AddOrder.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                }
                //read file from temp file, add at below
                //FileManager.retrieveTextContentAsStringArray(AddOrder.this,"/temp_folder");

            }
        });
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
            AlertDialog.Builder alert_no_order_file = new AlertDialog.Builder(AddOrder.this);
            alert_no_order_file.setTitle("Send Order");
            alert_no_order_file.setMessage("Are you sure want to save now?");
            alert_no_order_file.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Move to order folder
                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat changeDateFormat = new SimpleDateFormat("yyyyMMddhhmm");
                    String date = changeDateFormat.format(currentDate);
                    File order_temp_file = new File(FileManager.getPrivateDirPath(AddOrder.this) + File.separator + getString(R.string.temp_folder) + File.separator + getString(R.string.order_temp_file));
                    File order_file = new File(FileManager.getPublicDirPath(AddOrder.this) + File.separator + getString(R.string.order) + File.separator+ date+".txt");
                    order_temp_file.renameTo(order_file);
                    FileManager.removeTempFile(AddOrder.this, getString(R.string.order_temp_file));
                    Toast.makeText(AddOrder.this, "Save Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddOrder.this, MyOrders.class);
                    startActivity(intent);
                }
            });
            alert_no_order_file.setNegativeButton("No", null);
            alert_no_order_file.show();
        }
        return true;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Double getProductPrice = Double.parseDouble(productPrice.getText().toString());
        Double getProductQuantity= null;

        if (productQuantity.getText().toString().trim().length()>0){
            getProductQuantity= Double.parseDouble(productQuantity.getText().toString());
            Double totalPrices = getProductPrice * getProductQuantity;
            totalPrice.setText(totalPrices.toString());
        }
        else{
            totalPrice.setText("");
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

}
