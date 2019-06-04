package de.rvwbk.eit74.beaconapp.beacon;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;

public class BeaconDiscovery {

    public static final int[] BEACON_DARK_BLUE = {47140, 49207};
    public static final int[] BEACON_MINT = {62040, 5975};

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

    public void startDiscovery(Activity activity) {

       startDiscovery(activity, new int[2]);

    }

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
                }

                beaconManager.stopRanging(beaconRegion);
            }
        });


    }
}
