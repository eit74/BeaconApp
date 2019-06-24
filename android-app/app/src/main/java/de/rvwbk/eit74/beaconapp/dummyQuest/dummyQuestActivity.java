package de.rvwbk.eit74.beaconapp.dummyQuest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import de.rvwbk.eit74.beaconapp.R;
import de.rvwbk.eit74.beaconapp.unityStarter.unityStarterActivity;

public class dummyQuestActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_quest);

        TextView hint = findViewById(R.id.textViewHint);

        findViewById(R.id.btnSwitchToAR).setOnClickListener(e->{
            Intent i = new Intent(this, unityStarterActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btnAnswer1).setOnClickListener(e->{
            hint.setText("Richtig, es sagt \"Hello World !\".");
        });

        findViewById(R.id.btnAnswer2).setOnClickListener(e->{
            hint.setText("Falsch!");
        });

        findViewById(R.id.btnAnswer3).setOnClickListener(e->{
            hint.setText("Falsch!");
        });

        findViewById(R.id.btnAnswer4).setOnClickListener(e->{
            hint.setText("Falsch!");
        });


    }
}
