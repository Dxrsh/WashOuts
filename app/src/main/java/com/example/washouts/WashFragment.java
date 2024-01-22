package com.example.washouts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.washouts.firebase.FireBase;
import com.example.washouts.models.AddressModel;
import com.example.washouts.models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;


public class WashFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public WashFragment() {
        // Required empty public constructor
    }
    TextView addressInput,dateInput;
    String fullName,add="",loc="",land="",fullAddress,timeSlot="",serviceType="";
    AddressModel addressModel;
    UserModel userModel;
    Spinner spinner;
    RelativeLayout dry,wash,steam;
    ImageButton dryBtn,washBtn,steamBtn;
    Button schedule;
    EditText noOfGarments;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wash, container, false);

        addressInput = view.findViewById(R.id.mapInput);
        dateInput = view.findViewById(R.id.dateInput);
        spinner = view.findViewById(R.id.timeInput);
        dry = view.findViewById(R.id.dryButton);
        wash = view.findViewById(R.id.washButton);
        steam = view.findViewById(R.id.steamButton);
        dryBtn = view.findViewById(R.id.serviceInput1);
        washBtn = view.findViewById(R.id.serviceInput2);
        steamBtn = view.findViewById(R.id.serviceInput3);
        noOfGarments = view.findViewById(R.id.garmentsInput);
        schedule = view.findViewById(R.id.scheduleButton);

        spinner.setOnItemSelectedListener(this);


        addressInput.setOnClickListener(v -> getAddress());

        dateInput.setOnClickListener(v -> pickDate());

        pickTime();

        dryBtn.setOnClickListener(v -> {
            dry.setSelected(true);
            wash.setSelected(false);
            steam.setSelected(false);
            serviceType = "Dry Cleaning & Steam Press";
        });
        washBtn.setOnClickListener(v -> {
            wash.setSelected(true);
            dry.setSelected(false);
            steam.setSelected(false);
            serviceType = "Wash Cleaning & Steam Press";
        });
        steamBtn.setOnClickListener(v -> {
            steam.setSelected(true);
            wash.setSelected(false);
            dry.setSelected(false);
            serviceType = "Steam Press Only";
        });

        schedule.setOnClickListener(v -> {
            if (addressInput.getText().toString().isEmpty()) {
                addressInput.setError("Address is Required!");
                return;
            }
            if (dateInput.getText().toString().isEmpty()) {
                dateInput.setError("Pickup Date is Required!");
                return;
            }
            if (timeSlot.equals("Select")) {
                Toast.makeText(getActivity(), "Please Select PickUp Time!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serviceType.isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Service Type!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (noOfGarments.getText().toString().isEmpty()) {
                noOfGarments.setError("No of Garments should not be empty!");
                return;
            }
            if ((Integer.parseInt(noOfGarments.getText().toString()) > 10)) {
                noOfGarments.setError("No of Garments should not be more than 10!");
                return;
            }
            Intent intent = new Intent(getActivity(), CheckOutActivity.class);
            intent.putExtra("fullname",fullName);
            intent.putExtra("address",fullAddress);
            intent.putExtra("date",dateInput.getText().toString());
            intent.putExtra("time",timeSlot);
            intent.putExtra("service", serviceType);
            intent.putExtra("garments",noOfGarments.getText().toString());
            getActivity().startActivity(intent);
        });

        return view;
    }

    public void getAddress(){
        FireBase.getCurrentUserDetails().get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            userModel = documentSnapshot.toObject(UserModel.class);
                            if(userModel!=null){
                                addressModel = userModel.getAddress();
                                loc = addressModel.getLocation();
                                add = addressModel.getHouse();
                                land = addressModel.getLandmark();

                                fullAddress = add +", "+ land + ", "+loc;
                                addressInput.setText(add);

                                fullName = userModel.getFirstName()+userModel.getLastName();
                                checkAddress();
                            }
                        }
                    }
                });
    }

    public void checkAddress(){
        Intent intent;
        if(loc.isEmpty() && add.isEmpty()){
            intent = new Intent(getActivity(), MapActivity.class);
        } else {
            intent = new Intent(getActivity(),SavedAddressActivity.class);
        }
        getActivity().startActivity(intent);
    }

    public void pickDate() {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateInput.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },
                year,month,day);

        datePickerDialog.show();
    }

    public void pickTime() {
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(
                getActivity(),R.array.time_slots, android.R.layout.simple_spinner_item
        );
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        timeSlot = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "Please Select PickUp Time!", Toast.LENGTH_SHORT).show();
    }
}