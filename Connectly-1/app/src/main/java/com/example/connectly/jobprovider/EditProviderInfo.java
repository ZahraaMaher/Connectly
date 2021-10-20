package com.example.connectly.jobprovider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.connectly.R;
import com.example.connectly.SharedPrefManager;


public class EditProviderInfo extends Fragment {

    String companyName,phone,address,password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_edit_company_info, container, false);

        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        View viewer = inflater.inflate(R.layout.fragment_edit_company_info, container, false);
        Button updateButton = (Button) viewer.findViewById(R.id.updateButton);
        final EditText companyNameET = (EditText) viewer.findViewById(R.id.ET_ComName);
        final EditText phoneET = (EditText) viewer.findViewById(R.id.ET_ComPhoneNo);
        final EditText addressET = (EditText) viewer.findViewById(R.id.ET_Address);

        Cursor c = ProviderProfile.seekersDatabase.rawQuery("SELECT * FROM company WHERE email='"+ ProviderProfile.userEmail +"'",null);
        c.moveToFirst();

        companyName = c.getString(c.getColumnIndex("companyname"));
        phone = c.getString(c.getColumnIndex("phone"));
        address = c.getString(c.getColumnIndex("address"));
        password = c.getString(c.getColumnIndex("password"));


        if(!TextUtils.isEmpty(companyName)){
            companyNameET.setText(companyName);
        }

        if(!TextUtils.isEmpty(phone)){
            phoneET.setText(phone);
        }

        if(!TextUtils.isEmpty(address)){
            addressET.setText(address);
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View v) {

                companyName = companyNameET.getText().toString();
                phone = phoneET.getText().toString();
                address = addressET.getText().toString();

                SharedPrefManager.getSharedPrefManager(getActivity()).setLocation(address);

                ProviderProfile.seekersDatabase.execSQL("DELETE FROM company WHERE email='"+ ProviderProfile.userEmail +"'");
                ProviderProfile.seekersDatabase.execSQL("INSERT INTO company (email,password,companyname,phone,address) VALUES ('"+ ProviderProfile.userEmail +"','"+password+"','"+companyName+"','"+phone+"','"+address+"')");

                try {



                    ContentValues cv = new ContentValues();
                    cv.put("providerEmail", ProviderProfile.userEmail);
                    cv.put("providerName",companyName);

                    Log.e("providerName: ",companyName);
                    ProviderProfile.jobsDatabase.update("alljobs", cv, "providerEmail = ?", new String[]{ProviderProfile.userEmail});

                }catch (Exception e){

                }

                Intent intent = new Intent(getContext(), ProviderProfile.class);
                intent.putExtra("userEmail", ProviderProfile.userEmail);
                startActivity(intent);
                getActivity().finish();
            }
        });

        c.close();
        return viewer;

    }
}