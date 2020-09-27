package com.example.priyanka.mapsnearbyplaces.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Adapter.DashAdapter;
import com.example.priyanka.mapsnearbyplaces.Adapter.ManagerDashAdapter;
import com.example.priyanka.mapsnearbyplaces.CompanySalesmanActivity;
import com.example.priyanka.mapsnearbyplaces.Salesman.GraphActivity;
import com.example.priyanka.mapsnearbyplaces.Model.Company;
import com.example.priyanka.mapsnearbyplaces.Model.DashModel;
import com.example.priyanka.mapsnearbyplaces.Model.ManagerDashModel;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.util.FTPManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.net.ftp.FTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<DashModel> dashModelArrayList;
    ArrayList<ManagerDashModel> managerDashModelArrayList;

    private RecyclerView recyclerView;
    DashAdapter dashAdapter;
    ManagerDashAdapter managerDashAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private String currentUser = mAuth.getCurrentUser().getUid();
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView myemail,companyName;
    JSONObject jsonCompanyId = new JSONObject();
    private String comId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        recyclerView = findViewById(R.id.rv1);
        companyName = findViewById(R.id.textView_CompanyName);
        CardView cardView= findViewById(R.id.dashboard);
       // final String userId = mAuth.getCurrentUser().getUid();
        dashModelArrayList = new ArrayList<>();
        managerDashModelArrayList = new ArrayList<>();

       getUserType();
       getCompanyId();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FTPManager ftpManager= new FTPManager();
                String host_name = "infocomm.homelinux.com";
                ftpManager.ftpConnect(MenuActivity.this ,host_name,"nyk","nyk",8899);
                Snackbar.make(view, "Downloaded Successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        myemail = (TextView) header.findViewById(R.id.header_email);
        String email = mAuth.getCurrentUser().getEmail();
        myemail.setText(email);

    }

    @Override
    public void onStart(){
        super.onStart();

    }
    public void getCompanyId() {
        DocumentReference docRef=  db.collection("company_user").document(currentUser);
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
                }
                else{
                    Toast.makeText(MenuActivity.this, "ERROR!", Toast.LENGTH_SHORT).show();
                }
                //get salesman which same companyID with manager
            }

        });
    }

    public void getUserType(){
        CollectionReference company_user_CollectionRef = db.collection("company_user");
        String userID=mAuth.getCurrentUser().getUid();
        Query query = company_user_CollectionRef.whereEqualTo("userId", userID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userCat = document.get("userCategory").toString();
                         comId = document.get("companyId").toString();
                        companyName.setText(comId);
                        Toast.makeText(MenuActivity.this, userCat, Toast.LENGTH_SHORT).show();
                        if (userCat.equals("Manager")){
                            String admin_heads[] = {"View Location", "My Profile","Salesman Detail","Salesman Graph","Products","Their Expenses"};

                            String admin_subs[] = {"Show all Salesmen's location", "View Your Profile","My Company Salesmen","Salesman Graph","View Product","Request for expenses"};

                            int admin_images[] = {R.drawable.ic_maps, R.drawable.ic_profile,R.drawable.ic_customer,R.drawable.ic_graph,R.drawable.ic_menu_gallery,R.drawable.ic_menu_slideshow};
                            for(int count = 0 ; count < admin_heads.length ; count++)
                            {

                                ManagerDashModel managerDashModel = new ManagerDashModel();
                                managerDashModel.setHead(admin_heads[count]);
                                managerDashModel.setSub(admin_subs[count]);
                                managerDashModel.setImage(admin_images[count]);
                                managerDashModelArrayList.add(managerDashModel);
                                //this should be retrieved in our adapter
                            }
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                            managerDashAdapter = new ManagerDashAdapter(managerDashModelArrayList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(managerDashAdapter);

                        }else if (userCat.equals("Salesman")){
                            String heads[] = {"Maps", "My Profile","My Customer","My Orders","Plan Visits","My Expenses"};

                            String subs[] = {"Maps to show customer", "View Your Profile","View Customer","View Orders","Plan Tours","Claim the visits money"};

                            int images[] = {R.drawable.ic_maps, R.drawable.ic_profile,R.drawable.ic_customer,R.drawable.ic_graph,R.drawable.ic_menu_slideshow,R.drawable.ic_menu_gallery};

                            for(int count = 0 ; count < heads.length ; count++)
                            {
                                DashModel dashModel = new DashModel();
                                dashModel.setHead(heads[count]);
                                dashModel.setSub(subs[count]);
                                dashModel.setImage(images[count]);
                                dashModelArrayList.add(dashModel);
                                //this should be retrieved in our adapter
                            }
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                            dashAdapter = new DashAdapter(dashModelArrayList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(dashAdapter);
                        }
                    }
                }else{
                    Toast.makeText(MenuActivity.this, "ERROR!", Toast.LENGTH_SHORT).show();
                }
            }
            });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MenuActivity.this, MyProfile.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

               Intent intent = new Intent(MenuActivity.this, CompanySalesmanActivity.class);
               startActivity(intent);


        } else if (id == R.id.nav_calender) {
            Intent intent = new Intent(MenuActivity.this, GraphActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_my_customer) {

            Intent intent = new Intent(MenuActivity.this, CompanySalesmanActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Title");
            builder.setMessage("Are you want to logout?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null){
                                mAuth.signOut();
                                Toast.makeText(MenuActivity.this, user.getEmail()+ " Sign out!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MenuActivity.this, Login.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MenuActivity.this, "You aren't login Yet!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
