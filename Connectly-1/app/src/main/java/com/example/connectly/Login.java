package com.example.connectly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.connectly.jobprovider.ProviderHome;
import com.example.connectly.seeker.SeekerHome;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    public Button button;
    private EditText pass;
    private EditText userEmail;
    public TextView textViewNoSign;
    public Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.login);
        btn= findViewById(R.id.button4);
        pass= findViewById(R.id.editTextNumberPassword1);
        userEmail = findViewById(R.id.editTextTextEmailAddress4);
        textViewNoSign = findViewById(R.id.textViewNoSign);


        //(email,password,access,firstname,lastname,phone,age,dob,gender)
        // (company name, email, phone number, address)

        SQLiteDatabase myDataBase = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS seeker (email VARCHAR, password VARCHAR, firstname VARCHAR, lastname VARCHAR, phone VARCHAR, age VARCHAR, dob VARCHAR, gender VARCHAR, CVPath VARCHAR, CVDisplayName VARCHAR)");
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS company (email VARCHAR,password VARCHAR,companyname VARCHAR,phone VARCHAR,address VARCHAR)");

        Cursor c1 = myDataBase.rawQuery("SELECT * FROM seeker",null);
        Cursor c2 = myDataBase.rawQuery("SELECT * FROM company",null);

        int emailIndex1 = c1.getColumnIndex("email");
        int passIndex1 = c1.getColumnIndex("password");
        int emailIndex2 = c2.getColumnIndex("email");
        int passIndex2 = c2.getColumnIndex("password");
        int addresses = c2.getColumnIndex("address");

//        int accessIndex = c1.getColumnIndex("access");
//        int firstnameIndex = c1.getColumnIndex("firstname");

        c1.moveToFirst();
        c2.moveToFirst();

        final ArrayList<String> emails = new ArrayList<>();
        final ArrayList<String> passwords = new ArrayList<>();
        final ArrayList<String> locations = new ArrayList<>();
        final ArrayList<Integer> access = new ArrayList<>();

        while(!c1.isAfterLast()){
            emails.add(c1.getString(emailIndex1));
            passwords.add(c1.getString(passIndex1));
            locations.add("");
            access.add(2);
            c1.moveToNext();
        }

        while(!c2.isAfterLast()){
            emails.add(c2.getString(emailIndex2));
            passwords.add(c2.getString(passIndex2));
            locations.add(c2.getString(addresses));
            access.add(1);

            c2.moveToNext();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                check2();
//                check1();

                Intent intent;
                String email = userEmail.getText().toString();
                String password = pass.getText().toString();
                int cnt = 0;

                for(int i=0;i<emails.size();i++){
                    if(emails.get(i).equals(email) && passwords.get(i).equals(password)){
                        SharedPrefManager.getSharedPrefManager(Login.this).setEmail(email);
                        SharedPrefManager.getSharedPrefManager(Login.this).setPassword(password);

                        if(access.get(i) == 1) {
                            SharedPrefManager.getSharedPrefManager(Login.this).setLocation(locations.get(i));
                            intent = new Intent (Login.this, ProviderHome.class);
                            intent.putExtra("userEmail",email);
                            startActivity(intent);
                            cnt++;
                        }else{
                            intent = new Intent(Login.this, SeekerHome.class);
                            intent.putExtra("userEmail",email);
                            startActivity(intent);
                            cnt++;
                        }
                    }
                }
                if(cnt == 0) {
                    textViewNoSign.setText("Wrong Username Or Password");
                }
            }


        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });
    }
//    public void check1() {
//
//    }
//
//    public void check2(){
//        Intent intent;
//        if(pass.getText().toString().equals("Pr123"))
//        {intent = new Intent (Login.this, ProviderHome.class);
//            startActivity(intent);}
//    }

    }

