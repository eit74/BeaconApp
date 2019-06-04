package de.rvwbk.eit74.beaconapp.restconnection.object;

import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectCore;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

/**
 * BeaconObject
 *
 * @author Niclas titius
 */
public class TaskCompletionObject extends ObjectCore implements ObjectInterface {

    public TaskCompletionObject() {
        this.requiredKeys.add("user_id");
        this.requiredKeys.add("task_id");
        this.requiredKeys.add("timestamp");
    }

    @Override
    public String getObjectString() {
        return "TaskCompletions";
    }

    @Override
    public ObjectInterface getNew() {
        return new TaskCompletionObject();
    }
}
