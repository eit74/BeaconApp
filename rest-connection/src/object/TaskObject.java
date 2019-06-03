package object;

import object.struct.ObjectCore;
import object.struct.ObjectInterface;

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
