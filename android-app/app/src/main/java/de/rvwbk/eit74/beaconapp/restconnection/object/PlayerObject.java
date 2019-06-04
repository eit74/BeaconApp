package de.rvwbk.eit74.beaconapp.restconnection.object;

import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectCore;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

/**
 * BeaconObject
 *
 * @author Niclas titius
 */
public class PlayerObject extends ObjectCore implements ObjectInterface {


    public PlayerObject() {
        this.requiredKeys.add("name");
        this.requiredKeys.add("password");
    }

    @Override
    public String getObjectString() {
        return "Players";
    }

    @Override
    public ObjectInterface getNew() {
        return new PlayerObject();
    }

}
