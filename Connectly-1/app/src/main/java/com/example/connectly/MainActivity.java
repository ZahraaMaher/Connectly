package com.example.connectly;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.example.connectly.jobprovider.ProviderProfile;
import com.example.connectly.jobprovider.ProviderHome;
import com.example.connectly.seeker.SeekerHome;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    //2) define the time to wait before moving to login activity:
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LazyLoader loader = findViewById(R.id.lazyLoader);
        loader.setAnimDuration(500);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());

        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gotoNext();
            }
        },2500);


        // go to Post Job screen
        findViewById(R.id.splashLogo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProviderProfile.ViewRequsteActivity.class));
            }
        });
    }

    private void gotoNext(){
        String s_p = SharedPrefManager.getSharedPrefManager(MainActivity.this).getPassword();
        if(s_p.equals("")){
            Intent moveToLogin = new Intent(MainActivity.this,Login.class);
            startActivity(moveToLogin);
            finish();
        }else{
            login();
        }
    }

    private void login(){

        SQLiteDatabase myDataBase = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS seeker (email VARCHAR, password VARCHAR, firstname VARCHAR, lastname VARCHAR, phone VARCHAR, age VARCHAR, dob VARCHAR, gender VARCHAR)");
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS company (email VARCHAR,password VARCHAR,companyname VARCHAR,phone VARCHAR,address VARCHAR)");

        Cursor c1 = myDataBase.rawQuery("SELECT * FROM seeker",null);
        Cursor c2 = myDataBase.rawQuery("SELECT * FROM company",null);

        int emailIndex1 = c1.getColumnIndex("email");
        int passIndex1 = c1.getColumnIndex("password");

        int emailIndex2 = c2.getColumnIndex("email");
        int passIndex2 = c2.getColumnIndex("password");
        int address = c2.getColumnIndex("address");

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
            locations.add(c2.getString(address));
            access.add(1);
            c2.moveToNext();
        }

        Intent intent;
        String email = SharedPrefManager.getSharedPrefManager(MainActivity.this).getEmail();
        String password = SharedPrefManager.getSharedPrefManager(MainActivity.this).getPassword();
        int cnt = 0;

        for(int i=0;i<emails.size();i++){
            if(emails.get(i).equals(email) && passwords.get(i).equals(password)){
                SharedPrefManager.getSharedPrefManager(MainActivity.this).setEmail(email);
                SharedPrefManager.getSharedPrefManager(MainActivity.this).setPassword(password);

                if(access.get(i) == 1) {
                    SharedPrefManager.getSharedPrefManager(MainActivity.this).setLocation(locations.get(i));
                    intent = new Intent (MainActivity.this, ProviderHome.class);
                    intent.putExtra("userEmail",email);
                    startActivity(intent);
                    cnt++;
                }else{
                    intent = new Intent(MainActivity.this, SeekerHome.class);
                    intent.putExtra("userEmail",email);
                    startActivity(intent);
                    cnt++;
                }
            }
        }
        if(cnt == 0) {
            Intent moveToLogin = new Intent(MainActivity.this,Login.class);
            startActivity(moveToLogin);
            finish();
        }
    }
}