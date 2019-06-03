package object;

import object.struct.ObjectCore;
import object.struct.ObjectInterface;

public class BeaconObject extends ObjectCore implements ObjectInterface {


    public BeaconObject() {
        this.requiredKeys.add("name");
    }

    @Override
    public String getObjectString() {
        return "Beacons";
    }

    @Override
    public ObjectInterface getNew() {
        return new BeaconObject();
    }
}
