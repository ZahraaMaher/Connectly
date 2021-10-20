package com.example.connectly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectly.jobprovider.ProviderProfile;
import com.example.connectly.seeker.SeekerHome;
import com.example.connectly.seeker.SeekerProfile;

import java.util.ArrayList;

public class ApplyJob extends AppCompatActivity {

    public Button button;
    private Toolbar toolbar;
    public String seekerEmail, description;
    private SQLiteDatabase jobsDatabase, usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);

        //Attaching AppBar code
        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Apply Job");

        seekerEmail = getIntent().getStringExtra("userEmail");

        TextView aboutjobTV = findViewById(R.id.textView4);
        aboutjobTV.setText(description);
        TextView requirementsTV = findViewById(R.id.textView_Requirements);
        TextView emailTV = findViewById(R.id.textView8);
        TextView locationTV = findViewById(R.id.textView2);
        TextView CompanyNameTV = findViewById(R.id.TextView_CompanyNameShow);

        jobsDatabase = this.openOrCreateDatabase("Jobs", MODE_PRIVATE, null);
        jobsDatabase.execSQL("CREATE TABLE IF NOT EXISTS alljobs (providerEmail VARCHAR, providerName VARCHAR, jobTitle VARCHAR, jobDescription VARCHAR, jobRequirements VARCHAR, jobLocation VARCHAR ,seekerEmail VARCHAR)");


        usersDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
        usersDatabase.execSQL("CREATE TABLE IF NOT EXISTS seeker (email VARCHAR, password VARCHAR, firstname VARCHAR, lastname VARCHAR, phone VARCHAR, age VARCHAR, dob VARCHAR, gender VARCHAR, CVPath VARCHAR, CVDisplayName VARCHAR)");

//        Cursor c = myDatabase.rawQuery("SELECT * FROM alljobs",null);
//        c.moveToFirst();
//
//        String title1 = "";
//        String description1 = "";
//        String email1 = "";
//        String requirements1 = "";
//        String location1 = "";
//
//        while(!c.isAfterLast()){
//            title1 = c.getString(c.getColumnIndex("title"));
//            description1 = c.getString(c.getColumnIndex("description"));
//            email1 = c.getString(c.getColumnIndex("email"));
//            requirements1 = c.getString(c.getColumnIndex("requirements"));
//            location1 = c.getString(c.getColumnIndex("address"));
//            c.moveToNext();
//        }
//

        final String providerEmail = SharedPrefManager.getSharedPrefManager(this).g_Job.getProviderEmail();
        final String providerName = SharedPrefManager.getSharedPrefManager(this).g_Job.getJobProviderName();
        final String jobTitle = SharedPrefManager.getSharedPrefManager(this).g_Job.getJobTitle();
        final String jobDescription = SharedPrefManager.getSharedPrefManager(this).g_Job.getJobDescription();
        final String jobRequirements = SharedPrefManager.getSharedPrefManager(this).g_Job.getJobRequirements();
        final String jobLocation = SharedPrefManager.getSharedPrefManager(this).g_Job.getJobLocation();
        final String jobSeekerEmail = SharedPrefManager.getSharedPrefManager(this).g_Job.getSeekerEmail();

        emailTV.setText(providerEmail);
        CompanyNameTV.setText(providerName);
        aboutjobTV.setText(jobDescription);
        locationTV.setText(jobLocation);
        requirementsTV.setText(jobRequirements);

        button = (Button) findViewById(R.id.Apply);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Cursor jobsCursor = jobsDatabase.rawQuery("SELECT jobTitle FROM alljobs WHERE seekerEmail='" + seekerEmail + "'", null);


                ArrayList<String> seekerJobsTitles = new ArrayList<>();


                if (jobsCursor.getCount() > 0) {

                    jobsCursor.moveToFirst();


                    do {

                        seekerJobsTitles.add(jobsCursor.getString(jobsCursor.getColumnIndex("jobTitle")));

                    }

                    while (jobsCursor.moveToNext());

                    jobsCursor.close();


                     if (seekerJobsTitles.contains(jobTitle)) {
//                Toast.makeText(getBaseContext(), "This Username/Email already exist", Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(), "Can not apply for job again", Toast.LENGTH_LONG).show();

                        } else {

                            jobsDatabase.execSQL("INSERT INTO alljobs (providerEmail,providerName,jobTitle,jobDescription,jobRequirements,jobLocation,seekerEmail) VALUES ('" + providerEmail + "','" + providerName + "','" + jobTitle + "','" + jobDescription + "','" + jobRequirements + "','" + jobLocation + "','" + seekerEmail + "')");
                            sendApplyJobBroadcast();
                            finish();
                        }

                } else {

                    jobsDatabase.execSQL("INSERT INTO alljobs (providerEmail,providerName,jobTitle,jobDescription,jobRequirements,jobLocation,seekerEmail) VALUES ('" + providerEmail + "','" + providerName + "','" + jobTitle + "','" + jobDescription + "','" + jobRequirements + "','" + jobLocation + "','" + seekerEmail + "')");
                    sendApplyJobBroadcast();
                    finish();
                }



//                    if(TextUtils.isEmpty(seekerEmail)){
//
//                        ContentValues cv = new ContentValues();
//                        cv.put("seekerEmail",seekerEmail);
//
//                        jobsDatabase.update("alljobs", cv,"jobId = ?", new String[]{providerJobId});
//
//
//                    } else {





//
//                    }


                }



        });
    }

    //Attach menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //on Click on menu items:
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu1_home) {
            Intent intent = new Intent(ApplyJob.this, SeekerHome.class);
            intent.putExtra("userEmail", seekerEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu1_profile) {
            Intent intent = new Intent(ApplyJob.this, SeekerProfile.class);
            intent.putExtra("userEmail", seekerEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu1_logout) {
            SharedPrefManager.getSharedPrefManager(this).removeAllSharedPrefValue();
            Intent intent = new Intent(ApplyJob.this, Login.class);
            intent.putExtra("userEmail", "");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void sendApplyJobBroadcast() {

        Intent intent = new Intent("com.example.connectly.ApplyJob_ACTION");
        intent.putExtra("com.example.connectly.EXTRA_TEXT", "Broadcast received : Apply done Successfully");
        sendBroadcast(intent);
    }

    private BroadcastReceiver applayJobBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedText = intent.getStringExtra("com.example.connectly.EXTRA_TEXT");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("com.example.connectly.ApplyJob_ACTION");
        registerReceiver(applayJobBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(applayJobBroadcastReceiver);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
//    }


}