package de.rvwbk.eit74.beaconapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class RadarActivity extends AppCompatActivity {

    ImageView radarWave0 = null;
    ImageView radarWave1 = null;
    ImageView radarWave2 = null;
    ImageView radarWave3 = null;
    ImageView radarWave4 = null;
    Animation fadein_out = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);

        radarWave0 = findViewById(R.id.bg_radarwave_0);
        radarWave1 = findViewById(R.id.bg_radarwave_1);
        radarWave2 = findViewById(R.id.bg_radarwave_2);
        radarWave3 = findViewById(R.id.bg_radarwave_3);
        radarWave4 = findViewById(R.id.bg_radarwave_4);
        fadein_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_fadeout);

        radarWave0.setAnimation(fadein_out);
        radarWave1.setAnimation(fadein_out);
        radarWave2.setAnimation(fadein_out);
        radarWave3.setAnimation(fadein_out);
        radarWave4.setAnimation(fadein_out);
    }
}
