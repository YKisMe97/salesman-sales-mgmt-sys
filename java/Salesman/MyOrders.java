package com.example.priyanka.mapsnearbyplaces.Salesman;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.priyanka.mapsnearbyplaces.Adapter.OrderAdapter;
import com.example.priyanka.mapsnearbyplaces.Model.OrderInfo;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.Salesman.fragment.FragmentLocalOrder;
import com.example.priyanka.mapsnearbyplaces.Salesman.fragment.FragmentUploadedOrder;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyOrders extends AppCompatActivity {
    private FragmentLocalOrder fragmentLocalOrder;
    private FragmentUploadedOrder fragmentUploadedOrder;
    private CollectionPagerAdapter collectionPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private OrderAdapter orderAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_my_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("My Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setUpRecyclerView();
        fragmentLocalOrder = new FragmentLocalOrder();
        fragmentUploadedOrder = new FragmentUploadedOrder();
        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setSelectedTabIndicatorColor(Color.GRAY);

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
            Intent intent = new Intent(MyOrders.this, AddOrder.class);
            startActivity(intent);
        }
        return true;
    }
    public class CollectionPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
        public CollectionPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            Bundle args = new Bundle();
            switch(i){
                case 0:
                    return fragmentLocalOrder;

                default:
                case 1:
                    return fragmentUploadedOrder;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return getString(R.string.local);

                case 1:
                    return getString(R.string.uploaded);
            }
            return"";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

  /*  public void setUpRecyclerView() {
    Query q=db.collection("company_user").document(currentUser).collection("order");
        FirestoreRecyclerOptions<OrderInfo> options = new FirestoreRecyclerOptions.Builder<OrderInfo>().setQuery(q,OrderInfo.class).build();
        orderAdapter =new OrderAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.myOrderRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);
    }
    */

}