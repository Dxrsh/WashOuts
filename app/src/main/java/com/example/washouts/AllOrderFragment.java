package com.example.washouts;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.OrderModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllOrderFragment extends Fragment {

    public AllOrderFragment() {
        // Required empty public constructor
    }

    ListView listView;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_order, container, false);

        listView = view.findViewById(R.id.allOrderList);
        progressBar = view.findViewById(R.id.load);


        getAllOrders();

        return view;
    }

    private void getAllOrders() {
        FireBase.getOrderDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                List<OrderModel> data = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    OrderModel orderModel = documentSnapshot.toObject(OrderModel.class);
                    data.add(orderModel);
                }

                AdapterClass adapterClass = new AdapterClass(getContext(),data);

                listView.setAdapter(adapterClass);
            } else {
                Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class AdapterClass extends ArrayAdapter<OrderModel> {

        public AdapterClass(@NonNull Context context, List<OrderModel> data) {
            super(context, R.layout.all_list_layout, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView==null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.all_list_layout,parent,false);
            }

            TextView address = convertView.findViewById(R.id.displayAddTVO);
            TextView date = convertView.findViewById(R.id.displayDateTVO);
            TextView time = convertView.findViewById(R.id.displayTimeTVO);
            TextView service = convertView.findViewById(R.id.displayServiceTVO);
            TextView garments = convertView.findViewById(R.id.displayGarmentsTVO);
            TextView payment = convertView.findViewById(R.id.paymentTV);
            TextView mPayment = convertView.findViewById(R.id.modeOfPaymentTV);
            RelativeLayout completeOrder = convertView.findViewById(R.id.completeOrder);

            OrderModel currentOrderModel = getItem(position);

            if (currentOrderModel!=null) {
                address.setText(currentOrderModel.getFullAddress());
                date.setText(currentOrderModel.getPickUpDate());
                time.setText(currentOrderModel.getPickUpTime());
                service.setText(currentOrderModel.getServiceType());
                garments.setText(currentOrderModel.getNoOfGarments());
                payment.setText("Rs. "+currentOrderModel.getPayment());
                mPayment.setText("("+currentOrderModel.getModeOfPayment()+")");
            }

            completeOrder.setOnClickListener(v -> {
                FireBase.getAllUsers().document(currentOrderModel.getUserId())
                        .collection("orders").document(currentOrderModel.getOrderId())
                        .update("orderStatus","Completed")
                        .addOnCompleteListener(task -> {
                            Toast.makeText(getContext(), "Completed", Toast.LENGTH_SHORT).show();
                        });
                FireBase.getOrderDetails().whereEqualTo("orderId",currentOrderModel.getOrderId())
                        .get().addOnCompleteListener(task -> {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                FireBase.getOrderDetails().document(documentSnapshot.getId()).delete()
                                        .addOnCompleteListener(task1 -> {
                                            Toast.makeText(getContext(), "Order Removed", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });
            });

            return convertView;
        }
    }
}