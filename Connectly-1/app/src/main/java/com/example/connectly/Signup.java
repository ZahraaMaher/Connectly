package com.example.connectly;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.connectly.jobprovider.ProviderHome;
import com.example.connectly.seeker.SeekerHome;

import java.util.ArrayList;
import java.util.List;

public class Signup extends AppCompatActivity implements OnItemSelectedListener {
    private EditText password, confirm;
    private EditText userEmail;
    public Button btn;
    SQLiteDatabase myDataBase;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Button button = (Button) findViewById(R.id.signup);
        btn = (Button) findViewById(R.id.orlogin);
        password = (EditText) findViewById(R.id.editTextNumberPassword1);
        confirm = findViewById(R.id.editTextNumberPassword2);
        userEmail = (EditText) findViewById(R.id.editTextTextEmailAddress4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(this);

        List<String> type = new ArrayList<>();
        type.add("Seeker");
        type.add("Provider");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        myDataBase = Signup.this.openOrCreateDatabase("Users", MODE_PRIVATE, null);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPassword()) {
                    Intent intent;
                    myDataBase.execSQL("CREATE TABLE IF NOT EXISTS seeker (email VARCHAR, password VARCHAR, firstname VARCHAR, lastname VARCHAR, phone VARCHAR, age VARCHAR, dob VARCHAR, gender VARCHAR, CVPath VARCHAR, CVDisplayName VARCHAR)");
                    myDataBase.execSQL("CREATE TABLE IF NOT EXISTS company (email VARCHAR,password VARCHAR,companyname VARCHAR,phone VARCHAR,address VARCHAR)");

                    if (spinner.getSelectedItem().toString().equals("Seeker")) {
                        String email = userEmail.getText().toString();
                        String pass = password.getText().toString();

                        Cursor seekerCursor = myDataBase.rawQuery("SELECT * FROM seeker WHERE email='" + email + "'", null);
                        Cursor companyCursor = myDataBase.rawQuery("SELECT * FROM company WHERE email='" + email + "'", null);

                        if (seekerCursor.getCount() != 0 || companyCursor.getCount() != 0) {

                            Toast.makeText(getBaseContext(), "This Username/Email already exist", Toast.LENGTH_LONG).show();

                        } else {

                            SharedPrefManager.getSharedPrefManager(Signup.this).setEmail(email);
                            SharedPrefManager.getSharedPrefManager(Signup.this).setPassword(pass);
                            Toast.makeText(getBaseContext(), "You have register as a job seeker successfully!", Toast.LENGTH_LONG).show();


                            myDataBase.execSQL("INSERT INTO seeker (email,password,firstname,lastname,phone,age,dob,gender,CVPath,CVDisplayName) VALUES ('" + email + "','" + pass + "','','','','','','','','')");
                            intent = new Intent(Signup.this, SeekerHome.class);
                            intent.putExtra("data", String.valueOf(spinner.getSelectedItem()));
                            intent.putExtra("userEmail", email);
                            startActivity(intent);


                        }
                    } else {
                        String email = userEmail.getText().toString();
                        String pass = password.getText().toString();
                        Cursor seekerCursor = myDataBase.rawQuery("SELECT * FROM seeker WHERE email='" + email + "'", null);
                        Cursor companyCursor = myDataBase.rawQuery("SELECT * FROM company WHERE email='" + email + "'", null);

                        if (seekerCursor.getCount() != 0 || companyCursor.getCount() != 0) {
                            Toast.makeText(getBaseContext(), "This Username/Email already exist", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "You have register as a job provider successfully!", Toast.LENGTH_LONG).show();
                            // (company name, email, phone number, address)
                            SharedPrefManager.getSharedPrefManager(Signup.this).setEmail(email);
                            SharedPrefManager.getSharedPrefManager(Signup.this).setPassword(pass);
                            SharedPrefManager.getSharedPrefManager(Signup.this).setLocation("");


                            myDataBase.execSQL("INSERT INTO company (email,password,companyname,phone,address) VALUES ('" + email + "','" + pass + "','','','')");
                            intent = new Intent(Signup.this, ProviderHome.class);
                            intent.putExtra("userEmail", email);
                            intent.putExtra("data", String.valueOf(spinner.getSelectedItem()));
                            startActivity(intent);


                        }

                    }
                } else {
                    Toast.makeText(getBaseContext(), "Please enter password correctly in two fields.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean checkPassword() {
        String s_p = password.getText().toString().toLowerCase();
        String s_c = confirm.getText().toString().toLowerCase();
        if (s_p.equals(s_c)) return true;
        else return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}