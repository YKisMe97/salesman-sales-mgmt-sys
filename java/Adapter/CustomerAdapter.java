package com.example.priyanka.mapsnearbyplaces.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyanka.mapsnearbyplaces.Model.CustomerInfo;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.Salesman.CustomerDetail;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {
private OnItemClickListener onItemClickListener;
private Context mContext;
private ArrayList<String> myCustomerList;
public CustomerAdapter(ArrayList<String> customerList, Context context) {
        myCustomerList = customerList;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        String customerName = myCustomerList.get(position).replace(".txt","");
        holder.textViewCustomer.setText(customerName);
    }

    @Override
    public CustomerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_cardview,parent,false);
       return new CustomerHolder(v);
    }

    @Override
    public int getItemCount() {
        return myCustomerList.size();
    }

    class CustomerHolder extends RecyclerView.ViewHolder{
        TextView textViewCustomer;
        public CustomerHolder(final View itemView) {
            super(itemView);
            textViewCustomer = itemView.findViewById(R.id.textView_customer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onItemClickListener!=null){
                        //onItemClickListener.onItemClick(getSnapshots().getSnapshot(position),position);
                        String getCustName = myCustomerList.get(position);
                        Intent intent = new Intent(mContext, CustomerDetail.class);
                        intent.putExtra("custName",getCustName);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
   public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
       this.onItemClickListener = onItemClickListener;
   }
}
