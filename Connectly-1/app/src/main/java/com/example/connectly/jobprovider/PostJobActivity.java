package com.example.connectly.jobprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectly.Login;
import com.example.connectly.R;

import java.util.ArrayList;

public class PostJobActivity extends AppCompatActivity {
    //declare the components:
    Button postJobButton;
    Button clearDataButton;
    Button cancelButton;
    public EditText jobTitleET;
    public EditText jobDescriptionET;
    public EditText requirementsET;
    public EditText jobLocationET;
    public static SQLiteDatabase jobsDatabase,usersDatabase;
    private Toolbar toolbar;
    private String providerEmail,providerName;
    TextView CompanyNameShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        //define the buttons:
        postJobButton = findViewById(R.id.postJob);
        clearDataButton = findViewById(R.id.clearData);
        cancelButton = findViewById(R.id.cancelPostJob);
        jobTitleET = findViewById(R.id.jobTitleInput);
        jobDescriptionET = findViewById(R.id.jobDescriptionInput);
        requirementsET = findViewById(R.id.requirementsInput);
        jobLocationET = findViewById(R.id.JobLocationInput);
        CompanyNameShow = findViewById(R.id.TextView_CompanyNameShow);

        //define the toolbar
        toolbar = findViewById(R.id.myToolBar);
        toolbar.setTitle("Post Job");
        setSupportActionBar(toolbar);

        providerEmail = getIntent().getStringExtra("userEmail");


        jobsDatabase = this.openOrCreateDatabase("Jobs", MODE_PRIVATE, null);
        jobsDatabase.execSQL("CREATE TABLE IF NOT EXISTS alljobs (providerEmail VARCHAR, providerName VARCHAR, jobTitle VARCHAR, jobDescription VARCHAR, jobRequirements VARCHAR, jobLocation VARCHAR ,seekerEmail VARCHAR)");


        usersDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE,null);
        usersDatabase.execSQL("CREATE TABLE IF NOT EXISTS company (email VARCHAR,password VARCHAR,companyname VARCHAR,phone VARCHAR, address VARCHAR)");

        Cursor c = usersDatabase.rawQuery("SELECT * FROM company WHERE email='"+ providerEmail +"'",null);
        c.moveToFirst();

        providerName = c.getString(c.getColumnIndex("companyname"));


        if(TextUtils.isEmpty(providerName)){

            CompanyNameShow.setText("Please Update");

        }else {

            CompanyNameShow.setText(providerName);

        }

    }


    private boolean checkEmpty(){
        boolean rtn = false;

        if(jobTitleET.getText().toString().equals(""))return true;
        if(jobDescriptionET.getText().toString().equals(""))return true;
        if(requirementsET.getText().toString().equals(""))return true;

        return rtn;
    }

    //go to View Requests Activity by on click:
    public void postJobHandler(View view) {

        if(checkEmpty()){

            Toast.makeText(getBaseContext(), "Please fill the fields", Toast.LENGTH_LONG).show();

        }else{

            String jobTitle = jobTitleET.getText().toString();
            String jobDescription = jobDescriptionET.getText().toString();
            String jobRequirements = requirementsET.getText().toString();
            String jobLocation = jobLocationET.getText().toString();


            Cursor jobsCursor = jobsDatabase.rawQuery("SELECT jobTitle FROM alljobs WHERE providerEmail='" + providerEmail + "'", null);

            ArrayList<String> providerJobsTitles = new ArrayList<>();

            if (jobsCursor.getCount() > 0) {

                jobsCursor.moveToFirst();


                do {

                    providerJobsTitles.add(jobsCursor.getString(jobsCursor.getColumnIndex("jobTitle")));

                }

                while (jobsCursor.moveToNext());

                jobsCursor.close();


                    if (providerJobsTitles.contains(jobTitle)) {
//                Toast.makeText(getBaseContext(), "This Username/Email already exist", Toast.LENGTH_LONG).show();
                        Toast.makeText(this, "You add other job with same title, please enter other job title", Toast.LENGTH_SHORT).show();

                    } else {

                        jobsDatabase.execSQL("INSERT INTO alljobs (providerEmail,providerName,jobTitle,jobDescription,jobRequirements,jobLocation,seekerEmail) VALUES ('" + providerEmail + "','" + providerName + "','" + jobTitle + "','" + jobDescription + "','" + jobRequirements + "','" + jobLocation + "','')");
                        sendJobPostBroadcast();
                        finish();
                    }

            } else {

                jobsDatabase.execSQL("INSERT INTO alljobs (providerEmail,providerName,jobTitle,jobDescription,jobRequirements,jobLocation,seekerEmail) VALUES ('" + providerEmail + "','" + providerName + "','" + jobTitle + "','" + jobDescription + "','" + jobRequirements + "','" + jobLocation + "','')");
                sendJobPostBroadcast();
                finish();
            }

        }
    }//end of post job handler

    //cancelationHandler
    //go to Provider Home Activity by on click:
    public void cancelationHandler(View view) {
        Intent goToProviderHome = new Intent(PostJobActivity.this, ProviderHome.class);
        goToProviderHome.putExtra("userEmail", providerEmail);
        startActivity(goToProviderHome);
    }

    public void clearData(View view){
        jobTitleET.setText("");
        jobDescriptionET.setText("");
        requirementsET.setText("");
        jobLocationET.setText("");
    }


    //Attache menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    //on Click on menu items:
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id == R.id.menu2_home){
            Intent intent= new Intent(PostJobActivity.this,ProviderHome.class);
            intent.putExtra("userEmail", providerEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.Menu2_profile){
            Intent intent= new Intent(PostJobActivity.this, ProviderProfile.class);
            intent.putExtra("userEmail", providerEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu2_logout){
            Intent intent= new Intent(PostJobActivity.this, Login.class);
            intent.putExtra("userEmail", providerEmail);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void sendJobPostBroadcast() {

        Intent intent = new Intent("com.example.connectly.PostJob_ACTION");
        intent.putExtra("com.example.connectly.EXTRA_TEXT", "Broadcast received : New Job Posted");
        sendBroadcast(intent);
    }
    private BroadcastReceiver postJobBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedText = intent.getStringExtra("com.example.connectly.EXTRA_TEXT");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("com.example.connectly.PostJob_ACTION");
        registerReceiver(postJobBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(postJobBroadcastReceiver);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
//    }




}