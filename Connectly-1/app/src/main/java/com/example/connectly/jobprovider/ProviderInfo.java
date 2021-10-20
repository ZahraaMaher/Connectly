package com.example.connectly.jobprovider;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.connectly.R;


public class ProviderInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProviderInfo() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static ProviderInfo newInstance(String param1, String param2) {
        ProviderInfo fragment = new ProviderInfo();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_company_info, container, false);

        super.onCreateView(inflater,container,savedInstanceState);
        View viewer = inflater.inflate(R.layout.fragment_company_info, container, false);

        // Inflate the layout for this fragment
        TextView companyNameTextView = (TextView) viewer.findViewById(R.id.ET_ComName);
        TextView emailTextView = (TextView) viewer.findViewById(R.id.ET_ComEmail);
        TextView phoneTextView = (TextView) viewer.findViewById(R.id.ET_ComPhoneNo);
        TextView addressTextView = (TextView) viewer.findViewById(R.id.ET_Address);

        Cursor c = ProviderProfile.seekersDatabase.rawQuery("SELECT * FROM company WHERE email='"+ ProviderProfile.userEmail +"'",null);
        c.moveToFirst();

        String companyName = c.getString(c.getColumnIndex("companyname"));
        String email = c.getString(c.getColumnIndex("email"));
        String phone = c.getString(c.getColumnIndex("phone"));
        String address = c.getString(c.getColumnIndex("address"));

        if(!TextUtils.isEmpty(companyName)){
            companyNameTextView.setText(companyName);
        }
        else {
            companyNameTextView.setText("Please Update");
        }

        if(!TextUtils.isEmpty(email)){
            emailTextView.setText(email);
        }
        else {
            emailTextView.setText("Please Update");
        }

        if(!TextUtils.isEmpty(phone)){
            phoneTextView.setText(phone);
        }
        else {
            phoneTextView.setText("Please Update");
        }

        if(!TextUtils.isEmpty(address)){
            addressTextView.setText(address);
        }
        else {
            addressTextView.setText("Please Update");
        }


        c.close();
        return viewer;

    }
}