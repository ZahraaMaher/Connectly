package com.example.connectly.jobprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.connectly.Login;
import com.example.connectly.R;

public class ProviderHome extends AppCompatActivity {
    //1)Declear the components:
    Button PostJobBtn;
    Button ViewRequestsBtn;
    private Toolbar toolbar;
    SQLiteDatabase providerDatabase;
    private String providerEmail,providerName,providerPhone,providerAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home);

        //2) Define the bttons:
        PostJobBtn = findViewById(R.id.PostJob_providerHome);
        ViewRequestsBtn = findViewById(R.id.viewRequests_providerHome);

        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        providerEmail = getIntent().getStringExtra("userEmail");

        providerDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE,null);
        Cursor c = providerDatabase.rawQuery("SELECT * FROM company WHERE email='"+providerEmail+"'",null);
        c.moveToFirst();


        providerEmail = c.getString(c.getColumnIndex("email"));
        providerName = c.getString(c.getColumnIndex("companyname"));
        providerPhone = c.getString(c.getColumnIndex("phone"));
        providerAddress = c.getString(c.getColumnIndex("address"));


    }

    //4) move to View Requests Activity on Click:
    public void viewRequests_onClick(View view) {
        Intent moveToPostJob = new Intent(ProviderHome.this, ProviderProfile.ViewRequsteActivity.class);
        moveToPostJob.putExtra("userEmail", providerEmail);
        startActivity(moveToPostJob);
    }

    //5) move to Post Job Activity on click:
    public void postJob_onClick(View view) {
        Intent moveToViewRequests = new Intent(ProviderHome.this, PostJobActivity.class);
        moveToViewRequests.putExtra("userEmail", providerEmail);
        startActivity(moveToViewRequests);
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
            Intent intent= new Intent(ProviderHome.this,ProviderHome.class);
            intent.putExtra("userEmail", providerEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.Menu2_profile){
            Intent intent= new Intent(ProviderHome.this, ProviderProfile.class);
            intent.putExtra("userEmail", providerEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu2_logout){
            Intent intent= new Intent(ProviderHome.this, Login.class);
            intent.putExtra("userEmail", providerEmail);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}