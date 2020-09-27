package com.example.priyanka.mapsnearbyplaces.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyanka.mapsnearbyplaces.Model.VisitPlanInfo;
import com.example.priyanka.mapsnearbyplaces.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class VisitPlanAdapter extends FirestoreRecyclerAdapter<VisitPlanInfo,VisitPlanAdapter.VisitPlanHolder> {
    private OnItemClickListener onItemClickListener;
    public VisitPlanAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VisitPlanHolder holder, int position, @NonNull VisitPlanInfo model) {
        holder.textViewVisitPlan.setText("Visit Route");
    }

    @Override
    public VisitPlanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitplan_cardview,parent,false);
        return new VisitPlanHolder(v);
    }
    class VisitPlanHolder extends RecyclerView.ViewHolder{
        TextView textViewVisitPlan;
        public VisitPlanHolder(final View itemView) {
            super(itemView);
            textViewVisitPlan = itemView.findViewById(R.id.textView_visitPlan);

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


