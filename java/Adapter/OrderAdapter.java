package com.example.priyanka.mapsnearbyplaces.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Model.OrderInfo;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.util.FileManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    //private sqlDatabase mSqlDatabase;
    private Context mContext;
    private ArrayList<String> myOrderList;

    public OrderAdapter(ArrayList<String> orderList, Context context) {
        myOrderList = orderList;
        mContext = context;
    }
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderFileName;
        TextView textViewTotalPrice;
        TextView textViewTotalQuantity;
        CardView order_row;
        ImageButton del_btn;

        public OrderViewHolder(View itemView) {
            super(itemView);
            textViewOrderFileName = itemView.findViewById(R.id.order_file_name);
            textViewTotalPrice=itemView.findViewById(R.id.total_price);
            textViewTotalQuantity=itemView.findViewById(R.id.total_quantity);
            del_btn = itemView.findViewById(R.id.imgBtn_del);
            order_row = itemView.findViewById(R.id.local_order_card_row);
        }
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("FYP","On Create View");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_order_list_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        final String name = myOrderList.get(position);
        holder.textViewOrderFileName.setText(name);
        holder.textViewTotalPrice.setText("-");
        holder.textViewTotalQuantity.setText("-");
        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Deleted Order", Toast.LENGTH_SHORT).show();
            }
        });
           // Toast.makeText(mContext, "No Order Made Yet", Toast.LENGTH_SHORT).show()
        holder.order_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        //return mCursor.getCount();
        return myOrderList.size();
    }
}
