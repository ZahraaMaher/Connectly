package com.example.connectly.jobprovider;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.connectly.Login;
import com.example.connectly.R;
import com.example.connectly.SharedPrefManager;
import com.example.connectly.ViewPagerAdapter;
import com.example.connectly.seeker.Seeker;
import com.example.connectly.seeker.SeekerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProviderProfile extends AppCompatActivity {

    private Toolbar toolbar;
    public static String userEmail;
    public static SQLiteDatabase seekersDatabase,jobsDatabase;

    private boolean editable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_profile);

        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        userEmail = getIntent().getStringExtra("userEmail");
        seekersDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE,null);
        seekersDatabase.execSQL("CREATE TABLE IF NOT EXISTS company (email VARCHAR,password VARCHAR,companyname VARCHAR,phone VARCHAR, address VARCHAR)");


        jobsDatabase = this.openOrCreateDatabase("Jobs", MODE_PRIVATE, null);
        jobsDatabase.execSQL("CREATE TABLE IF NOT EXISTS alljobs (providerEmail VARCHAR, providerName VARCHAR, jobTitle VARCHAR, jobDescription VARCHAR, jobRequirements VARCHAR, jobLocation VARCHAR ,seekerEmail VARCHAR)");


        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem tabInfoC = findViewById(R.id.tabInfoCom);
        TabItem tabEinfoC = findViewById(R.id.tabEditInfoCom);
        final ViewPager companyViewPager = findViewById(R.id.viewPager);

        ProviderPagerAdapter pagerAdapter = new ProviderPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        companyViewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                companyViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }});
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
            Intent intent= new Intent(ProviderProfile.this, ProviderHome.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.Menu2_profile){
            Intent intent= new Intent(ProviderProfile.this, ProviderProfile.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu2_logout){
            Intent intent= new Intent(ProviderProfile.this, Login.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ViewRequsteActivity extends AppCompatActivity {

        private Toolbar toolbar;
        public Button viewBtn;
        public String userEmail;
        private TextView txt_no_users;

        ListView lst_seekers;
        private List<Seeker> seekersList = new ArrayList<>();

        private SeekerAdapter seekerAdapter;
        private SeekerAdapter.SeekersListAdapterCallback seeker_callback = new SeekerAdapter.SeekersListAdapterCallback() {
            @Override
            public void onClickView(int indx) {

                if(!TextUtils.isEmpty(seekersList.get(indx).CVPath)) {

                    Intent intent = new Intent(ViewRequsteActivity.this, CVActivity.class);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("CVFullPath", seekersList.get(indx).CVPath);

                    startActivity(intent);
                }

                else {

                    Toast.makeText(ViewRequsteActivity.this, "The seeker not upload cv", Toast.LENGTH_SHORT).show();
                }
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_request);
            viewBtn = findViewById(R.id.viewId);

            toolbar = findViewById(R.id.myToolBar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("View Requests");

            txt_no_users = findViewById(R.id.txt_no_users);
            txt_no_users.setVisibility(View.GONE);

            lst_seekers = findViewById(R.id.lst_seekers);

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.AddFragment(new viewRequestsFragment(), "Requests");
            userEmail = SharedPrefManager.getSharedPrefManager(ViewRequsteActivity.this).getEmail();

            seekerAdapter = new SeekerAdapter(ViewRequsteActivity.this, seekersList, seeker_callback);
            lst_seekers.setAdapter(seekerAdapter);

            loadSeekers();

        }

        private void loadSeekers(){

            seekersList.clear();

            SQLiteDatabase JobsDatabase = this.openOrCreateDatabase("Jobs", MODE_PRIVATE, null);
            SQLiteDatabase SeekersDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);

            JobsDatabase.execSQL("CREATE TABLE IF NOT EXISTS alljobs (providerEmail VARCHAR, providerName VARCHAR, jobTitle VARCHAR, jobDescription VARCHAR, jobRequirements VARCHAR, jobLocation VARCHAR ,seekerEmail VARCHAR)");

            Cursor allJobsForJobsProvider = JobsDatabase.rawQuery("SELECT * FROM alljobs WHERE providerEmail='"+ userEmail +"'",null);


            if(allJobsForJobsProvider.getCount() > 0){

                allJobsForJobsProvider.moveToFirst();

                while (!allJobsForJobsProvider.isAfterLast()){

                String seekerEmail = allJobsForJobsProvider.getString(allJobsForJobsProvider.getColumnIndex("seekerEmail"));
//                Toast.makeText(this, "seekerEmail : "+seekerEmail, Toast.LENGTH_SHORT).show();

                if(seekerEmail != null && !seekerEmail.equals("") ){

                    Cursor seekerCursor = SeekersDatabase.rawQuery("SELECT * FROM seeker WHERE email='"+seekerEmail+"'",null);

                    String seekerName = "";

                    String  cvPath = "";
                    if (seekerCursor.getCount() > 0){

                        seekerCursor.moveToFirst();

                        seekerName = seekerCursor.getString(seekerCursor.getColumnIndex("firstname")) +" "+seekerCursor.getString(seekerCursor.getColumnIndex("lastname"));
                        cvPath = seekerCursor.getString(seekerCursor.getColumnIndex("CVPath")) ;

                    }

                    String jobTitle = allJobsForJobsProvider.getString(allJobsForJobsProvider.getColumnIndex("jobTitle"));


                    Seeker seeker = new Seeker();
                    seeker.name = seekerName;
                    seeker.title = jobTitle;
                    seeker.CVPath = cvPath;
                    seekersList.add(seeker);

                }

                allJobsForJobsProvider.moveToNext();

                }

            }


            seekerAdapter.notifyDataSetChanged();

            if(seekersList.size() == 0){
                txt_no_users.setVisibility(View.VISIBLE);
                lst_seekers.setVisibility(View.GONE);

            }else{
                txt_no_users.setVisibility(View.GONE);
                lst_seekers.setVisibility(View.VISIBLE);
            }
        }

        public void ViewCV_onClick(View view) {
            Intent moveToRequest= new Intent(ViewRequsteActivity.this, CVActivity.class);
            moveToRequest.putExtra("userEmail", userEmail);
            startActivity(moveToRequest);
        }

        //Attache menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu2, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();

            if (id == R.id.menu2_home){
                Intent intent= new Intent(ViewRequsteActivity.this, ProviderHome.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                return true;
            } else if (id == R.id.Menu2_profile){
                Intent intent= new Intent(ViewRequsteActivity.this, ProviderProfile.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                return true;
            } else if (id == R.id.menu2_logout){
                SharedPrefManager.getSharedPrefManager(ViewRequsteActivity.this).removeAllSharedPrefValue();
                Intent intent= new Intent(ViewRequsteActivity.this,Login.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    public static class viewRequestsFragment extends Fragment {

        View view;
        public viewRequestsFragment() {

        }
        @Nullable
        @Override
        public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_view_requests, container, false);
            return view;

        }
    }
}
