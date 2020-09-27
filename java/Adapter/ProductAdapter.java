package com.example.priyanka.mapsnearbyplaces.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Model.ProductInfo;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.storage.sqlDatabase;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    //private ArrayList<String> list = new ArrayList<String>();
    private sqlDatabase mSqlDatabase;
    private Context mContext;
    private Cursor mCursor;
    private int productId;

    public ProductAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProduct;
        TextView textViewQuantity;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewProduct = itemView.findViewById(R.id.textView_product);
            textViewQuantity=itemView.findViewById(R.id.textView_quantity);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_cardview, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        if(!mCursor.moveToNext()){
            return;
        }else{
            holder.textViewProduct.setText(mCursor.getString( mCursor.getColumnIndex("productName")));
            holder.textViewQuantity.setText("RM "+mCursor.getString(mCursor.getColumnIndex("productQuantity")));
        }

    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    /*  @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mSqlDatabase = new sqlDatabase(context);;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_cardview, null);

            Cursor data = mSqlDatabase.getProductList();
            if (data.getCount()==0){
                Toast.makeText(context, "There is no any product", Toast.LENGTH_SHORT).show();
            }
            else{
                while (data.moveToNext()){
                    productId = data.getInt(0);
                }
            }

            TextView textViewProduct = view.findViewById(R.id.textView_product);
            TextView textViewQuantity=view.findViewById(R.id.textView_quantity);
            textViewProduct.setText("");
            textViewQuantity.setText("");
            return view;
        }
    */

}
