package com.example.connectly.jobprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.connectly.Login;
import com.example.connectly.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class CVActivity extends AppCompatActivity {

    //1)Declear the components:
    private PDFView cv;
    private Toolbar toolbar;
    public String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        //2) Set the toolbar title:
        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request");

        //3) load the cv pdf file:
        cv = findViewById(R.id.pdfCv);
//        cv.fromAsset("resume_tamplet.pdf").load();

        String cvFullPath = getIntent().getStringExtra("CVFullPath");
        Log.e( "info: ", "File Path " +cvFullPath+" , File Exist : "+new File(cvFullPath).exists());


        cv.fromFile(new File(cvFullPath)).load();

        userEmail = getIntent().getStringExtra("userEmail");

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
            Intent intent= new Intent(CVActivity.this, ProviderHome.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.Menu2_profile){
            Intent intent= new Intent(CVActivity.this, ProviderProfile.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu2_logout){
            Intent intent= new Intent(CVActivity.this, Login.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}