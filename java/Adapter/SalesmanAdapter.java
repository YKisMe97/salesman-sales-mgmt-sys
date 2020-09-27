package com.example.priyanka.mapsnearbyplaces.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyanka.mapsnearbyplaces.Model.Salesmen;
import com.example.priyanka.mapsnearbyplaces.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class SalesmanAdapter extends FirestoreRecyclerAdapter<Salesmen,SalesmanAdapter.SalesmenHolder> {
    private SalesmanAdapter.OnItemClickListener onItemClickListener;
    public SalesmanAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SalesmenHolder holder, int position, @NonNull Salesmen model) {
        holder.textViewSalesman.setText(model.getUserCategory());
    }

    @Override
    public SalesmenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.salesmen_card_view,parent,false);
        return new SalesmenHolder(v);
    }
    class SalesmenHolder extends RecyclerView.ViewHolder{
        TextView textViewSalesman;
        public SalesmenHolder(final View itemView) {
            super(itemView);
            textViewSalesman = itemView.findViewById(R.id.textView_salesman);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onItemClickListener!=null){
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(SalesmanAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
