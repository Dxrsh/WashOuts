package com.example.washouts;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    public OrderFragment() {
        // Required empty public constructor
    }
    ListView listView;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        listView = view.findViewById(R.id.orderList);
        progressBar = view.findViewById(R.id.load);


        getOrders();
        return view;
    }

    private void getOrders() {
        FireBase.getUsersOrders().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                    }
                });
    }

    public static class AdapterClass extends ArrayAdapter<OrderModel> {

        public AdapterClass(@NonNull Context context, List<OrderModel> data) {
            super(context, R.layout.list_layout, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView==null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_layout,parent,false);
            }

            TextView address = convertView.findViewById(R.id.displayAddTVO);
            TextView date = convertView.findViewById(R.id.displayDateTVO);
            TextView time = convertView.findViewById(R.id.displayTimeTVO);
            TextView service = convertView.findViewById(R.id.displayServiceTVO);
            TextView garments = convertView.findViewById(R.id.displayGarmentsTVO);
            TextView payment = convertView.findViewById(R.id.paymentTV);
            TextView mPayment = convertView.findViewById(R.id.modeOfPaymentTV);
            RelativeLayout relativeLayout = convertView.findViewById(R.id.circle);
            TextView orderStatus = convertView.findViewById(R.id.orderStatus);
            RelativeLayout removeOrder = convertView.findViewById(R.id.removeOrder);

            OrderModel currentOrderModel = getItem(position);

            if (currentOrderModel!=null) {
                address.setText(currentOrderModel.getFullAddress());
                date.setText(currentOrderModel.getPickUpDate());
                time.setText(currentOrderModel.getPickUpTime());
                service.setText(currentOrderModel.getServiceType());
                garments.setText(currentOrderModel.getNoOfGarments());
                payment.setText("Rs. "+currentOrderModel.getPayment());
                mPayment.setText("("+currentOrderModel.getModeOfPayment()+")");
                orderStatus.setText(currentOrderModel.getOrderStatus());
            }

            if (currentOrderModel.getOrderStatus().equals("Active")) {
                int blueColor = ContextCompat.getColor(getContext(),R.color.main);
                relativeLayout.getBackground().setColorFilter(blueColor, PorterDuff.Mode.SRC_ATOP);
            } else if (currentOrderModel.getOrderStatus().equals("Completed")){
                int greenColor = ContextCompat.getColor(getContext(), com.google.android.libraries.places.R.color.quantum_googgreen);
                relativeLayout.getBackground().setColorFilter(greenColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                int defaultColor = ContextCompat.getColor(getContext(), com.google.android.libraries.places.R.color.places_ui_default_primary_dark);
                relativeLayout.getBackground().setColorFilter(defaultColor, PorterDuff.Mode.SRC_ATOP);
            }

            removeOrder.setOnClickListener(v -> {
                FireBase.getUsersOrders().document(currentOrderModel.getOrderId())
                        .delete().addOnCompleteListener(task -> {
                            Toast.makeText(getContext(), "Order Removed", Toast.LENGTH_SHORT).show();
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