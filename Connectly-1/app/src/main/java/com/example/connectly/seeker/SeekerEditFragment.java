package com.example.connectly.seeker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectly.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SeekerEditFragment extends Fragment {

    private static final int REQUEST_CODE_SELECT_PDF = 1;
    private static final String TAG = "SeekerEditFragment";
    String email, password, firstName, lastName, phone, age, dob, gender, CVPath, CVDisplayName;
    TextView cvLable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View viewer = inflater.inflate(R.layout.activity_seeker_edit_fragment, container, false);
        Button updateButton = (Button) viewer.findViewById(R.id.updateButton);
        final EditText firstNameET = (EditText) viewer.findViewById(R.id.EditFName);
        final EditText lastNameET = (EditText) viewer.findViewById(R.id.ET_lName);
        final EditText phoneET = (EditText) viewer.findViewById(R.id.ET_SeekerPhone);
        final EditText ageET = (EditText) viewer.findViewById(R.id.ET_age);
        final EditText dobET = (EditText) viewer.findViewById(R.id.ET_bDate);
        final RadioButton male = (RadioButton) viewer.findViewById(R.id.male);
        final RadioButton female = (RadioButton) viewer.findViewById(R.id.female);
        final Button browseCV = (Button) viewer.findViewById(R.id.browseCVButton);
        cvLable = (TextView) viewer.findViewById(R.id.TV_CV);


//        ArrayList<String> docs = new ArrayList<>();
//        docs.add(DocPicker.DocTypes.PDF);
//        docs.add(DocPicker.DocTypes.MS_POWERPOINT);
//        docs.add(DocPicker.DocTypes.MS_EXCEL);
//        docs.add(DocPicker.DocTypes.TEXT);
//
//
//        final DocPickerConfig pickerConfig = new DocPickerConfig()
//                .setAllowMultiSelection(false)
//                .setShowConfirmationDialog(true)
//                .setExtArgs(docs);


        browseCV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getActivity(), NormalFilePickActivity.class);
//                intent.putExtra(Constant.MAX_NUMBER, 1);
//                intent.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"pdf"});
//                startActivityForResult(intent, Constant.REQUEST_CODE_PICK_FILE);
//

//                DocPicker.with(getActivity())
//                        .setConfig(pickerConfig)
//                        .onResult()
//                        .subscribe(new Observer<ArrayList<Uri>>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//                            }
//
//                            @Override
//                            public void onNext(ArrayList<Uri> uris) {
//                                //uris: list of uri
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                            }
//
//                            @Override
//                            public void onComplete() {
//                            }
//                        });


//

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("application/pdf");
                    startActivityForResult(intent, 2);

                } else {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_SELECT_PDF);

                }


            }
        });

        Cursor c = SeekerProfile.myDatabase.rawQuery("SELECT * FROM seeker WHERE email='" + SeekerProfile.userEmail + "'", null);
        c.moveToFirst();


        email = c.getString(c.getColumnIndex("email"));
        password = c.getString(c.getColumnIndex("password"));
        firstName = c.getString(c.getColumnIndex("firstname"));
        lastName = c.getString(c.getColumnIndex("lastname"));
        phone = c.getString(c.getColumnIndex("phone"));
        age = c.getString(c.getColumnIndex("age"));
        dob = c.getString(c.getColumnIndex("dob"));
        gender = c.getString(c.getColumnIndex("gender"));
        CVPath = c.getString(c.getColumnIndex("CVPath"));
        CVDisplayName = c.getString(c.getColumnIndex("CVDisplayName"));


        if (!TextUtils.isEmpty(firstName)) {
            firstNameET.setText(firstName);
        }

        if (!TextUtils.isEmpty(lastName)) {
            lastNameET.setText(lastName);
        }

        if (!TextUtils.isEmpty(phone)) {
            phoneET.setText(phone);
        }
        if (!TextUtils.isEmpty(age)) {
            ageET.setText(age);
        }

        if (!TextUtils.isEmpty(dob)) {
            dobET.setText(dob);
        }

        if (!TextUtils.isEmpty(CVDisplayName)) {
            cvLable.setText(CVDisplayName);
        }

        if (!TextUtils.isEmpty(gender)) {

            if (gender.equals("Male")) {
                male.setChecked(true);
            } else {
                female.setChecked(true);
            }

        } else {

            male.setChecked(false);
            female.setChecked(false);

        }


        updateButton.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View v) {


                firstName = firstNameET.getText().toString();
                lastName = lastNameET.getText().toString();
                phone = phoneET.getText().toString();
                age = ageET.getText().toString();
                dob = dobET.getText().toString();

                Boolean maleStatus = male.isChecked();
                Boolean femaleStatus = female.isChecked();

                if (!maleStatus && !femaleStatus) {
                    gender = "";
                } else if (maleStatus) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }


                Log.e("Paths : ", CVPath + " " + CVDisplayName);
                SeekerProfile.myDatabase.execSQL("DELETE FROM seeker WHERE email='" + SeekerProfile.userEmail + "'");
                SeekerProfile.myDatabase.execSQL("INSERT INTO seeker (email,password,firstname,lastname,phone,age,dob,gender,CVPath,CVDisplayName) VALUES ('" + SeekerProfile.userEmail + "','" + password + "','" + firstName + "','" + lastName + "','" + phone + "','" + age + "','" + dob + "','" + gender + "','" + CVPath + "','" + CVDisplayName + "')");


                Intent intent = new Intent(getContext(), SeekerProfile.class);
                intent.putExtra("userEmail", SeekerProfile.userEmail);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return viewer;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CODE_SELECT_PDF) {


            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("application/pdf");
            startActivityForResult(intent, 2);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                // Get the Uri of the selected file
                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                CVPath = data.getData().getPath();
//                    String path = getPDFPath(uri);

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {

                        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

                        if (cursor != null && cursor.moveToFirst()) {


                            int indexDisplayNameColumn = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            CVDisplayName = cursor.getString(indexDisplayNameColumn);
                            CVPath = openPath(uri, CVDisplayName);
                            cvLable.setText(CVDisplayName);

//                            Toast.makeText(getActivity(), "Path" + CVPath, Toast.LENGTH_SHORT).show();

//                            File yourFile = new File(dir, path +"/"+ displayName);

//
//                            ContentValues contentValues = new ContentValues();
////                            contentValues.put("CVPath", yourFile.getPath());
//                            contentValues.put("CVPath", path);
//                            contentValues.put("CVDisplayName",displayName);
//                            Toast.makeText(getActivity(), "File Path : " +path , Toast.LENGTH_SHORT).show();
//
//                            String[] args = new String[]{SeekerProfile.username};
//                            SeekerProfile.myDatabase.update("seeker", contentValues, "email=?", args);
//                            Toast.makeText(getActivity(), "File Path : " + CVPath + ", File Name : " + CVDisplayName, Toast.LENGTH_SHORT).show();

                        }
                    } finally {
                        cursor.close();
                    }

                } else if (uriString.startsWith("file://")) {
                    CVDisplayName = myFile.getName();
                    CVPath = myFile.getAbsolutePath();
                    cvLable.setText(CVDisplayName);

                    Toast.makeText(getActivity(), "File Path : " + CVPath + ", File Name : " + CVDisplayName, Toast.LENGTH_SHORT).show();

                }
                break;
        }


    }

    public String openPath(Uri uri, String CVDisplayName) {

        InputStream inputStream = null;

//        File mydir = getActivity().getDir("SeekersCVs", Context.MODE_PRIVATE); //Creating an internal dir;

//        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        File dir = Environment.getExternalStoragePublicDirectory("Documents");

        File mydir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "SeekersCVs");

        if (!mydir.exists()) {
            mydir.mkdir();
        }

        File fileWithinMyDir = new File(mydir, CVDisplayName); //Getting a file within the dir.


        try {
            inputStream = getActivity().getContentResolver().openInputStream(uri);
            //Convert your stream to data here

//            BufferedReader br = null;
//
//            try {
//                // read this file into InputStream
//                inputStream = new FileInputStream(dir + File.separator + "SeekersCVs" + File.separator + CVDisplayName);
//                br = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder sb = new StringBuilder();
//
//                String line;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                System.out.println(sb.toString());
//                System.out.println("\nDone!");
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (br != null) {
//                    try {
//                        br.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//
//            inputStream.close();


            FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual

//            OutputStream out = getActivity().getContentResolver().openOutputStream(uri);

            copyFromUri(getActivity(), inputStream, out, uri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return mydir.getPath() + File.separator + CVDisplayName;
    }

    public static void copyFromUri(@NonNull Context context, @NonNull InputStream inputStream, @NonNull OutputStream outputStream, @NonNull Uri uri) throws IOException {
        try {

            final byte[] buffer = new byte[2084];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

        }
    }


}