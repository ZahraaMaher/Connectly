package com.example.connectly.seeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.connectly.Login;
import com.example.connectly.R;
import com.example.connectly.SharedPrefManager;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class SeekerProfile extends AppCompatActivity {

    private Toolbar toolbar;
    public static String userEmail;
    public static SQLiteDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_profile);

        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Seeker Profile");

        userEmail = getIntent().getStringExtra("userEmail");
        myDatabase = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS seeker (email VARCHAR, password VARCHAR, firstname VARCHAR, lastname VARCHAR, phone VARCHAR, age VARCHAR, dob VARCHAR, gender VARCHAR)");

        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem infoTab = findViewById(R.id.tabInfoSeek);
        TabItem modifyTab = findViewById(R.id.tabEditInfoSeek);
        final ViewPager viewPager = findViewById(R.id.viewPager);

        SeekerPagerAdapter seekerPagerAdapter = new SeekerPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(seekerPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    //Attache menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //on Click on menu items:
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id == R.id.menu1_home){
            Intent intent= new Intent(SeekerProfile.this, SeekerHome.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu1_profile){
            Intent intent= new Intent(SeekerProfile.this,SeekerProfile.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu1_logout){
            SharedPrefManager.getSharedPrefManager(SeekerProfile.this).removeAllSharedPrefValue();
            Intent intent= new Intent(SeekerProfile.this, Login.class);
            intent.putExtra("userEmail","");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


