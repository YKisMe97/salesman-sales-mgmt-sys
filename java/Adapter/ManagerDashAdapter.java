package com.example.priyanka.mapsnearbyplaces.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.priyanka.mapsnearbyplaces.SalesManager.ClaimList;
import com.example.priyanka.mapsnearbyplaces.CompanySalesmanActivity;
import com.example.priyanka.mapsnearbyplaces.Salesman.GraphActivity;
import com.example.priyanka.mapsnearbyplaces.Model.ManagerDashModel;
import com.example.priyanka.mapsnearbyplaces.activity.MyProfile;
import com.example.priyanka.mapsnearbyplaces.Product;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.SalesManager.SalesmanLocation;

import java.util.ArrayList;


public class ManagerDashAdapter extends RecyclerView.Adapter<ManagerDashAdapter.ViewHolder> {

    ArrayList<ManagerDashModel> managerDashModelArrayList;

    ImageView images;
    public ManagerDashAdapter(ArrayList<ManagerDashModel> managerDashModelArrayList) {
        this.managerDashModelArrayList = managerDashModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard,parent,false);
        final ViewHolder holder= new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =  holder.getAdapterPosition();
                switch (position){
                    case 0:
                        view.getContext().startActivity( new Intent(view.getContext(),SalesmanLocation.class));
                        break;

                    case 1:
                        view.getContext().startActivity( new Intent(view.getContext(),MyProfile.class));
                        break;
                    case 2:
                        view.getContext().startActivity( new Intent(view.getContext(),CompanySalesmanActivity.class));
                        break;
                    case 3:
                        view.getContext().startActivity( new Intent(view.getContext(),GraphActivity.class));
                        break;
                    case 4:
                        view.getContext().startActivity( new Intent(view.getContext(),Product.class));
                        break;
                    case 5:
                        view.getContext().startActivity( new Intent(view.getContext(),ClaimList.class));
                        break;
                }



            }
        });
        return holder;
        //return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String ret_head = managerDashModelArrayList.get(position).getHead();
        holder.setheader(ret_head);

        String ret_sub = managerDashModelArrayList.get(position).getSub();
        holder.set_sub(ret_sub);

        int ret_image = managerDashModelArrayList.get(position).getImage();
        holder.set_image(ret_image);
    }

    @Override
    public int getItemCount() {
        return managerDashModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView header,sub_header;
        View myView;

        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setheader(String h)
        {
            header = myView.findViewById(R.id.header);
            header.setText(h);
        }

        public void set_sub(String s)
        {
            sub_header = myView.findViewById(R.id.sub_header);
            sub_header.setText(s);
        }
        public void set_image(int i)
        {
            images = myView.findViewById(R.id.dash_image);
            images.setImageResource(i);
        }


    }
}
