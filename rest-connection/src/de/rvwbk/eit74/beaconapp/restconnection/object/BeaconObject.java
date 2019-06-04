package de.rvwbk.eit74.beaconapp.restconnection.object;

import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectCore;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

/**
 * BeaconObject
 *
 * @author Niclas titius
 */
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
