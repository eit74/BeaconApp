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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.rvwbk.eit74.beaconapp.beacon.BeaconDiscovery;
import de.rvwbk.eit74.beaconapp.dummyQuest.dummyQuestActivity;
import de.rvwbk.eit74.beaconapp.restconnection.connection.AsyncConnection;
import de.rvwbk.eit74.beaconapp.restconnection.object.TaskCompletionObject;
import de.rvwbk.eit74.beaconapp.restconnection.object.TaskObject;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

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

                Toast.makeText(getApplicationContext(),beaconID,Toast.LENGTH_LONG).show();

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

        // Start BeaconScanner Listener
        registerReceiver();

        // Init BeaconScanner
        BeaconDiscovery bd = BeaconDiscovery.getInstance(getApplicationContext());
        bd.startDiscovery(this);

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

    /**
     * retrieves the lowest uncompleted task for the current user
     * @return next task id
     */
    private Triple<String, Integer, Object> getNextTask () {
        // get all TaskCompletions for current user
        AsyncConnection ac = new AsyncConnection();
        TaskCompletionObject tco = new TaskCompletionObject();
        List<ObjectInterface> list = ac.get(tco, "user_id", UserData.getInstance().getUserID());
        Set<String> completedTasks = new HashSet<>();

        // get all Tasks
        AsyncConnection ac2 = new AsyncConnection();
        TaskObject to = new TaskObject();
        List<ObjectInterface> taskObjectList = ac2.get(to);

        // parse all Tasks
        List<Triple<String, Integer, Object>> taskList = new ArrayList<>();
        for (ObjectInterface oi : taskObjectList) {
            if (oi instanceof TaskObject) {
                TaskObject task = (TaskObject) oi;

                int thing = new Integer (task.get("queuePos"));
                //                                                       // ID   // POS // task
                Triple<String, Integer, Object> triple = new Triple(task.get("id"), thing, task);
                taskList.add(triple);
            } else {
                System.out.println("ERROR");
            }
        }

        // mark completed tasks
        List<Triple<String, Integer, Object>> completedTaskObjects = new LinkedList<>();
        for (String completedTask : completedTasks) {
            for (Triple<String, Integer, Object> triple : taskList) {
                if (triple.x.equals(completedTask)) {
                    completedTaskObjects.add(triple);
                }
            }
        }

        // remove completed tasks
        for (Triple<String, Integer, Object> completed : completedTaskObjects) {
            taskList.remove(completed);
        }

        // select lowest
        Triple<String, Integer, Object> next = taskList.get(0);
        for (Triple<String, Integer, Object> task : taskList) {
            if (next.y > task.y) {
                next = task;
            }
        }
        //completedTasks.forEach(s -> taskList.remove(taskList.stream().filter(t -> t.x == s).findFirst().orElse(null)));

        UserData.getInstance().setNextTask(next.x);
        return next;
    }

    class Triple<X, Y, Z> {
        X x;
        Y y;
        Z z;

        public Triple (X x, Y y, Z z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
