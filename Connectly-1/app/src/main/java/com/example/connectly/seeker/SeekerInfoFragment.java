package com.example.connectly.seeker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectly.R;
import com.example.connectly.jobprovider.CVActivity;

public class SeekerInfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View viewer = inflater.inflate(R.layout.activity_seeker_info_fragment, container, false);

        // Inflate the layout for this fragment
        TextView firstNameTextView = (TextView) viewer.findViewById(R.id.TextView_firstNameShow);
        TextView lastNameTextView = (TextView) viewer.findViewById(R.id.TextView_LastNameShow);
        TextView emailTextView = (TextView) viewer.findViewById(R.id.TextView_EmailShow);
        TextView phoneTextView = (TextView) viewer.findViewById(R.id.TextView_PhoneShow);
        TextView ageTextView = (TextView) viewer.findViewById(R.id.TextView_AgeShow);
        TextView dobTextView = (TextView) viewer.findViewById(R.id.TextView_BithOfDateShow);
        TextView genderTextView = (TextView) viewer.findViewById(R.id.TextView_GenderShow);
        final TextView cvTextView = (TextView) viewer.findViewById(R.id.TextView_CV);


        Cursor c = SeekerProfile.myDatabase.rawQuery("SELECT * FROM seeker WHERE email='" + SeekerProfile.userEmail + "'", null);
        c.moveToFirst();

        String firstName = c.getString(c.getColumnIndex("firstname"));
        String lastName = c.getString(c.getColumnIndex("lastname"));
        String email = c.getString(c.getColumnIndex("email"));
        String phone = c.getString(c.getColumnIndex("phone"));
        String age = c.getString(c.getColumnIndex("age"));
        String dob = c.getString(c.getColumnIndex("dob"));
        String gender = c.getString(c.getColumnIndex("gender"));
        final String CVDisplayName = c.getString(c.getColumnIndex("CVDisplayName"));
        final String cvPath = c.getString(c.getColumnIndex("CVPath"));
        Log.e("cvPath: ", cvPath);

        if(!TextUtils.isEmpty(firstName)){
            firstNameTextView.setText(firstName);
        }

        else {
            firstNameTextView.setText("Please Update");
        }

        if(!TextUtils.isEmpty(lastName)){
            lastNameTextView.setText(lastName);
        }
        else {
            lastNameTextView.setText("Please Update");
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

        if(!TextUtils.isEmpty(age)){
            ageTextView.setText(age);
        }
        else {
            ageTextView.setText("Please Update");
        }


        if(!TextUtils.isEmpty(dob)){
            dobTextView.setText(dob);
        }
        else {
            dobTextView.setText("Please Update");
        }

        if(!TextUtils.isEmpty(gender)){
            genderTextView.setText(gender);
        }
        else {
            genderTextView.setText("Please Update");
        }

        if(!TextUtils.isEmpty(CVDisplayName)){
            cvTextView.setText(CVDisplayName);
        }
        else {
            cvTextView.setText("Please Update");
        }




        cvTextView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                if(!CVDisplayName.equals("")){
                    Intent viewCV = new Intent(getActivity(), CVActivity.class);
                    viewCV.putExtra("userEmail", SeekerProfile.userEmail);
                    viewCV.putExtra("CVFullPath",cvPath);
                    startActivity(viewCV);

//                    File file = new File(cvPath);
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    startActivity(intent);

                }
                else {
                    Toast.makeText(getActivity(), "Please Upload CV", Toast.LENGTH_SHORT).show();
                }

            }
        });


        c.close();

        return viewer;
    }
}