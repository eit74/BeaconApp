package object;

import object.struct.ObjectCore;
import object.struct.ObjectInterface;

public class TaskCompletionObject extends ObjectCore implements ObjectInterface {

    public TaskCompletionObject() {
        this.requiredKeys.add("user_id");
        this.requiredKeys.add("task_id");
        this.requiredKeys.add("timestamp");
    }

    @Override
    public String getObjectString() {
        return null;
    }

    @Override
    public ObjectInterface getNew() {
        return null;
    }
}
