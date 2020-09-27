package com.example.priyanka.mapsnearbyplaces.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyanka.mapsnearbyplaces.Model.Expenses;
import com.example.priyanka.mapsnearbyplaces.Model.Salesmen;
import com.example.priyanka.mapsnearbyplaces.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ClaimListAdapter extends FirestoreRecyclerAdapter<Expenses,ClaimListAdapter.ClaimListHolder> {

    private OnItemClickListener onItemClickListener;
    public ClaimListAdapter(@NonNull FirestoreRecyclerOptions<Expenses> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClaimListHolder holder, int position, @NonNull Expenses model) {

        holder.textViewClaimList.setText(Double.toString(model.getAmount()));

    }

    @NonNull
    @Override
    public ClaimListHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.claimlist_cardview,parent,false);
        return new ClaimListHolder(v);
    }

    class ClaimListHolder extends RecyclerView.ViewHolder{
        TextView textViewClaimList;
        public ClaimListHolder(final View itemView) {
            super(itemView);
            textViewClaimList = itemView.findViewById(R.id.textView_ClaimList);

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
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
