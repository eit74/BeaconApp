package de.rvwbk.eit74.beaconapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, LoginActivity.class);
      /*
        Questaufruf zum Testen
        String questID = "5cf63a3b7eae9301a728314e";
        String playerIDbonki = "5cf6180e7eae9301a728314b";
        Intent i = new Intent(this, DefaultQuestActivity.class);
        i.putExtra("TaskID", questID);
        i.putExtra("PlayerID", playerIDbonki);
        */
        startActivity(i);
    }
}
