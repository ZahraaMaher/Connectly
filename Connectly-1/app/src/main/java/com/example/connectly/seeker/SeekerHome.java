package com.example.connectly.seeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.connectly.ApplyJob;
import com.example.connectly.jobprovider.Job;
import com.example.connectly.jobprovider.JobsAdapter;
import com.example.connectly.Login;
import com.example.connectly.R;
import com.example.connectly.SharedPrefManager;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;


public class SeekerHome extends AppCompatActivity {

    private Toolbar toolbar;
    private String userEmail;

    List<Job> jobs = new ArrayList<>();
    /*private TabLayout tabLayout;
    private ViewPager viewPager;*/

    private TextView txt_no_jobs;

    ListView listView;
    String mTitle[] = {"Testing", "IOs", "Android and Java developer", "Web developer"};
    String mDescription[] = {"SS&co", "Adc", "eco", "cmp"};

    public Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home);

        txt_no_jobs = findViewById(R.id.txt_no_users);

        userEmail = getIntent().getStringExtra("userEmail");

        //Attaching AppBar code
        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        listView = findViewById(R.id.list_view);
        loadJobs();

        //TabLayout code
       /* tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewPager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new FragmentMatched(), "Jobs Feed");
        adapter.AddFragment(new FragmentRecommended(), "Recommended");
        //Adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);*/
    }

    //move to Apply job on Click Apply Button:
    public void ApplyOnClick(View view) {
//        Intent moveToApplyJob= new Intent(SeekerHome.this,ApplyJob.class);
//        moveToApplyJob.putExtra("username",username);
//        startActivity(moveToApplyJob);
    }

    //Attache menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private JobsAdapter.JobAdapterCallback jobAdapterCallback = new JobsAdapter.JobAdapterCallback() {
        @Override
        public void onClickApply(int indx) {

            Intent intnt = new Intent(SeekerHome.this, ApplyJob.class);
            intnt.putExtra("userEmail", userEmail);
            SharedPrefManager.getSharedPrefManager(SeekerHome.this).g_Job.setProviderEmail(jobs.get(indx).getProviderEmail());
            SharedPrefManager.getSharedPrefManager(SeekerHome.this).g_Job.setJobProviderName(jobs.get(indx).getJobProviderName());
            SharedPrefManager.getSharedPrefManager(SeekerHome.this).g_Job.setJobDescription(jobs.get(indx).getJobDescription());
            SharedPrefManager.getSharedPrefManager(SeekerHome.this).g_Job.setJobRequirements(jobs.get(indx).getJobRequirements());
            SharedPrefManager.getSharedPrefManager(SeekerHome.this).g_Job.setJobLocation(jobs.get(indx).getJobLocation());
            SharedPrefManager.getSharedPrefManager(SeekerHome.this).g_Job.setJobTitle(jobs.get(indx).getJobTitle());
            SharedPrefManager.getSharedPrefManager(SeekerHome.this).g_Job.setSeekerEmail(jobs.get(indx).getSeekerEmail());

            startActivity(intnt);
        }
    };

    private void loadJobs() {

        SQLiteDatabase myDatabase;
        myDatabase = SeekerHome.this.openOrCreateDatabase("Jobs", MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS alljobs (providerEmail VARCHAR, providerName VARCHAR, jobTitle VARCHAR, jobDescription VARCHAR, jobRequirements VARCHAR, jobLocation VARCHAR ,seekerEmail VARCHAR)");

//        Cursor seekerJobs = myDatabase.rawQuery("SELECT * FROM alljobs WHERE seekerEmail!='" + userEmail + "'", null);
        Cursor newJobs = myDatabase.rawQuery("SELECT * FROM alljobs WHERE seekerEmail=''", null);

//        DatabaseUtils.dumpCursorToString(seekerJobs);
        DatabaseUtils.dumpCursorToString(newJobs);

        jobs.clear();

//        if (seekerJobs.moveToFirst()) {
//            do
//            {
//                Job job = new Job();
//                job.setProviderEmail(seekerJobs.getString(0));
//                job.setJobProviderName(seekerJobs.getString(1));
//                job.setJobTitle(seekerJobs.getString(2));
//                job.setJobDescription(seekerJobs.getString(3));
//                job.setJobRequirements(seekerJobs.getString(4));
//                job.setJobLocation(seekerJobs.getString(5));
//                job.setSeekerEmail(seekerJobs.getString(6));
//
//                jobs.add(job);
//            }
//            while (seekerJobs.moveToNext());
//        }
//        seekerJobs.close();


        if (newJobs.getCount() > 0) {

            Log.e("loadJobs: ","Job counts : "+newJobs.getCount());

            newJobs.moveToFirst();

            do {
                Job job = new Job();
                job.setProviderEmail(newJobs.getString(0));
                job.setJobProviderName(newJobs.getString(1));
                job.setJobTitle(newJobs.getString(2));
                job.setJobDescription(newJobs.getString(3));
                job.setJobRequirements(newJobs.getString(4));
                job.setJobLocation(newJobs.getString(5));
                job.setSeekerEmail(newJobs.getString(6));

                jobs.add(job);
            }

            while (newJobs.moveToNext());
            newJobs.close();

            txt_no_jobs.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

        } else {

            Log.e("loadJobs: ","Job counts : "+newJobs.getCount());

            txt_no_jobs.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }


        JobsAdapter jobsAdapter = new JobsAdapter(this, R.layout.home_row, jobs, jobAdapterCallback);

        listView.setAdapter(jobsAdapter);


    }

    //on Click on menu items:
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu1_home) {
            Intent intent = new Intent(SeekerHome.this, SeekerHome.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu1_profile) {
            Intent intent = new Intent(SeekerHome.this, SeekerProfile.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu1_logout) {
            SharedPrefManager.removeAllSharedPrefValue();
            Intent intent = new Intent(SeekerHome.this, Login.class);
            intent.putExtra("userEmail", "");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

