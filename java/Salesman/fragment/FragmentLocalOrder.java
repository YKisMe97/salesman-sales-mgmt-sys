package com.example.priyanka.mapsnearbyplaces.Salesman.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.mapsnearbyplaces.Adapter.OrderAdapter;
import com.example.priyanka.mapsnearbyplaces.R;
import com.example.priyanka.mapsnearbyplaces.util.FTPManager;
import com.example.priyanka.mapsnearbyplaces.util.FileManager;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the\
 * to handle interaction events.
 * Use the {@link FragmentLocalOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLocalOrder extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView local_recyclerView;
    private OrderAdapter orderAdapter;
    private TextView total_order_files;
    private FloatingActionButton fab_upload_order;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentLocalOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLocalOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLocalOrder newInstance(String param1, String param2) {
        FragmentLocalOrder fragment = new FragmentLocalOrder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_order, container, false);
        String local_order_path = FileManager.getPublicDirPath(getContext()) + File.separator + getString(R.string.order);
        File order_directory = new File(local_order_path);
        if (!order_directory.exists()){
            order_directory.mkdirs();
        }
        File[] order_files = order_directory.listFiles();
        int total_order_files_length=order_files.length;
        ArrayList<String> arrayString = new ArrayList<>();
        for (int i = 0; i < total_order_files_length; i++) {
            arrayString.add(order_files[i].getName());
        }
        fab_upload_order = view.findViewById(R.id.fab_upload_order);
        local_recyclerView = view.findViewById(R.id.order_recylerView);
        local_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        local_recyclerView.setHasFixedSize(true);
        local_recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        orderAdapter = new OrderAdapter(arrayString, getContext());
        local_recyclerView.setAdapter(orderAdapter);

        total_order_files = view.findViewById(R.id.total_order_files);
        total_order_files.setText("Total Orders: "+total_order_files_length);

        uploadOrder();
        return view;
    }

    //Upload Order Files
    private void uploadOrder(){
        fab_upload_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_no_order_file = new AlertDialog.Builder(getActivity());
                alert_no_order_file.setTitle("Send Order");
                alert_no_order_file.setMessage("Are you sure want to send now?");
                alert_no_order_file.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String RemoteDirPath = "/KUAN/order/my_order/";
                            FTPClient ftpClient = null;
                            ftpClient.setConnectTimeout(10 * 1000);
                            ftpClient.connect(getContext().getString(R.string.ftp_hostname), 8899);
                            ftpClient.login(getContext().getString(R.string.ftp_username), getContext().getString(R.string.ftp_username));

                            String localFilePath = FileManager.getPrivateDirPath(getContext()) + File.separator + getString(R.string.order);
                            File order_files = new File(localFilePath);
                            if (!order_files.exists()) {
                                order_files.mkdirs();
                            }
                            File[] order_filesList = order_files.listFiles();
                            int files_length = order_filesList.length;
                            for (int j=0;j<files_length;j++){
                                File file = order_filesList[j];
                                String file_name = file.getAbsolutePath();
                                FTPManager.uploadFile(ftpClient, file_name, RemoteDirPath);

                                String string_uploadedFilePath = FileManager.getPrivateDirPath(getContext()) + File.separator + getString(R.string.transfer_order);
                                File uploadedFilePath = new File(string_uploadedFilePath);
                                file.renameTo(uploadedFilePath);

                            }


                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                    }
                });
                alert_no_order_file.setNegativeButton("No", null);
                alert_no_order_file.show();
            }
        });
    }
    private void getOrderTotalPriceAndQuantity(){

    }

}
