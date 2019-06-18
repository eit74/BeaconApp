package de.rvwbk.eit74.beaconapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import de.rvwbk.eit74.beaconapp.beacon.BeaconDiscovery;
import de.rvwbk.eit74.beaconapp.dummyQuest.dummyQuestActivity;

public class RadarActivity extends AppCompatActivity {

    private final BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager lbm;

    private final RadarActivity meself;

    public RadarActivity(){
        this.meself = this;
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(!intent.getAction().equals(BeaconDiscovery.ACTION_BEACON_FOUND)) {
                    return;
                }
                // TODO: Quest aus der DB holen und mit den Daten die QuestActivity starten...
                String beaconID = intent.getStringExtra("beaconID");

                if(beaconID.equals(BeaconDiscovery.BEACON_DARK_BLUE_ID)) {
                    Intent i = new Intent(meself, dummyQuestActivity.class);
                    startActivity(i);
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);

        lbm = LocalBroadcastManager.getInstance(getApplicationContext());

        // Init BeaconScanner
        BeaconDiscovery bd = BeaconDiscovery.getInstance(getApplicationContext());
        bd.startDiscovery(this);

        // Start BeaconScanner Listener
        registerReceiver();

        // Start Radar Animation
        initRadarAnimation();
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver();
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver();
    }

    private void initRadarAnimation(){
        Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_fadeout);

        findViewById(R.id.bg_radarwave_0).setAnimation(a);
        findViewById(R.id.bg_radarwave_1).setAnimation(a);
        findViewById(R.id.bg_radarwave_2).setAnimation(a);
        findViewById(R.id.bg_radarwave_3).setAnimation(a);
        findViewById(R.id.bg_radarwave_4).setAnimation(a);
    }

    private void registerReceiver(){
        lbm.registerReceiver(broadcastReceiver, new IntentFilter(BeaconDiscovery.ACTION_BEACON_FOUND));
    }

    private void unregisterReceiver(){
        lbm.unregisterReceiver(broadcastReceiver);
    }
}
