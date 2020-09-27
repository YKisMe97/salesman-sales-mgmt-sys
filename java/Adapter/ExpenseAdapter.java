package com.example.priyanka.mapsnearbyplaces.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyanka.mapsnearbyplaces.Model.Expenses;
import com.example.priyanka.mapsnearbyplaces.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ExpenseAdapter extends FirestoreRecyclerAdapter<Expenses,ExpenseAdapter.ExpensesHolder> {
    private OnItemClickListener onItemClickListener;
    public ExpenseAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExpensesHolder holder, int position, @NonNull Expenses model) {
        holder.textViewExpenses.setText(Double.toString(model.getAmount()));
    }

    @Override
    public ExpensesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_cardview,parent,false);
        return new ExpensesHolder(v);
    }

    class ExpensesHolder extends RecyclerView.ViewHolder{
        TextView textViewExpenses;
        public ExpensesHolder(final View itemView) {
            super(itemView);
            textViewExpenses = itemView.findViewById(R.id.textView_Expenses);

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
