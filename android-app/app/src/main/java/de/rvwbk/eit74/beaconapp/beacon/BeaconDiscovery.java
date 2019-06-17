package de.rvwbk.eit74.beaconapp.beacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;

/**
 *
 *  Here is some code you can use in your activity to catch the event when a beacon is found.
 *
 *  The intent contains the id of the beacon which was found.
 *
 *
    BroadcastReceiver connectionLostReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(!intent.getAction().equals(BeaconDiscovery.ACTION_BEACON_FOUND)) {
                return;
            }

            String beaconID = intent.getStringExtra("beaconID");

            if(beaconID.equals(BeaconDiscovery.BEACON_DARK_BLUE_ID)) {

                //It's the dark blue beacon

            } else if(beaconID.equals(BeaconDiscovery.BEACON_MINT_ID)) {

                //It's the mint beacon

            }

            // Do your thing here

        }
    };

    LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(connectionLostReceiver, new IntentFilter(BeaconDiscovery.ACTION_BEACON_FOUND));


 */

public class BeaconDiscovery {

    public static final int[] BEACON_DARK_BLUE = {47140, 49207};
    public static final int[] BEACON_MINT = {62040, 5975};

    public static final String BEACON_DARK_BLUE_ID = "47140-49207";
    public static final String BEACON_MINT_ID = "62040-5975";

    public static final String ACTION_BEACON_FOUND = "de.rvwbk.eit74.beaconapp.beacon.BeaconDiscovery.ACTION_BEACON_FOUND";

    private static BeaconDiscovery ourInstance = null;

    public static BeaconDiscovery getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new BeaconDiscovery(context);
        }

        return ourInstance;
    }

    private BeaconDiscovery(Context context) {
        this.beaconManager = new BeaconManager(context);
    }

    private BeaconManager beaconManager = null;
    private boolean isStarted = false;

    /*
        This function starts beacon discovery.
        All beacons which were found, will send a broadcast you can react to.

        Use this function if you want to get all beacons which are next to you.
     */
    public void startDiscovery(Activity activity) {

       startDiscovery(activity, new int[2]);

    }

    /*
        This function starts discovery for a specific beacon.
        use the public constants for beacons as the beacon you are looking for.

        Use this if you want to get a specific beacon.
     */
    public void startDiscovery(Activity activity, int[] beacon) {

        if(!this.isStarted) {
            start(activity, beacon);
            return;
        }

        SystemRequirementsChecker.checkWithDefaultDialogs(activity);

        BeaconRegion region = new BeaconRegion("discoveredRegion", null, beacon[0], beacon[1]);

        this.beaconManager.startRanging(region);
    }

    private void start(final Activity activity, final int[] beacon) {

        this.beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                isStarted = true;
                startDiscovery(activity, beacon);
            }
        });

        this.beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                for (Beacon b : beacons) {
                    String id = b.getUniqueKey();
                    String uuid = b.getProximityUUID().toString();
                    String major = Integer.toString(b.getMajor());
                    String minor = Integer.toString(b.getMinor());

                    Toast.makeText(activity, major + "\n" + minor, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(activity, uuid, Toast.LENGTH_LONG).show();

                    sendBroadcast(activity, major + "-" + minor);
                }

                beaconManager.stopRanging(beaconRegion);

            }
        });


    }

    private void sendBroadcast(Activity activity, String id) {
        Intent intent = new Intent();

        intent.putExtra("beaconID", id);
        intent.setAction(ACTION_BEACON_FOUND);

        LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
    }


}
