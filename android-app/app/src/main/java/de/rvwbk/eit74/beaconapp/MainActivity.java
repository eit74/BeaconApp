package de.rvwbk.eit74.beaconapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String questID = "123";
        Intent i = new Intent(this, DefaultQuestActivity.class);
        i.putExtra("QuestID", questID);
        startActivity(i);
    }
}
