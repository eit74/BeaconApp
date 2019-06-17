package de.rvwbk.eit74.beaconapp.unityStarter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.DefaultCompany.ARDemo.UnityPlayerActivity;

import de.rvwbk.eit74.beaconapp.R;

public class unityStarterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity_starter);

        Intent i = new Intent(this, UnityPlayerActivity.class);
        startActivity(i);
    }
}
