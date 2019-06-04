package de.rvwbk.eit74.beaconapp.restconnection.object;

import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectCore;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

/**
 * BeaconObject
 *
 * @author Niclas titius
 */
public class TaskObject extends ObjectCore implements ObjectInterface {

    public TaskObject() {
        this.requiredKeys.add("queuePos");
        this.requiredKeys.add("beacon_id");
    }

    @Override
    public String getObjectString() {
        return "Tasks";
    }

    @Override
    public ObjectInterface getNew() {
        return new TaskObject();
    }
}
