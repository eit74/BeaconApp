package de.rvwbk.eit74.beaconapp.beacon;

import android.app.Activity;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;

public class BeaconManagerService {
    private static final BeaconManagerService ourInstance = new BeaconManagerService();

    public static BeaconManagerService getInstance() {
        return ourInstance;
    }

    private BeaconManagerService() {
    }

    private BeaconManager manager = null;

    public void start(final Activity activity) {
        SystemRequirementsChecker.checkWithDefaultDialogs(activity);

        this.manager = new BeaconManager(activity.getApplicationContext());

        this.manager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                manager.startMonitoring(new BeaconRegion(
                        "monitored region",
                        null,
                        null, null));
            }
        });

        this.manager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {

                String id = beacons.get(0).getUniqueKey();

                Toast.makeText(activity.getApplicationContext(), id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExitedRegion(BeaconRegion region) {
                Toast.makeText(activity, region.getIdentifier(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
