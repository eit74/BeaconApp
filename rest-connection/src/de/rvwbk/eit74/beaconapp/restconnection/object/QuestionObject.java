package de.rvwbk.eit74.beaconapp.restconnection.object;

import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectCore;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

/**
 * BeaconObject
 *
 * @author Niclas titius
 */
public class QuestionObject extends ObjectCore implements ObjectInterface {

    public QuestionObject() {
        this.requiredKeys.add("question");
        this.requiredKeys.add("answer1");
        this.requiredKeys.add("answer2");
        this.requiredKeys.add("correctAnswer");
    }

    @Override
    public String getObjectString() {
        return "Questions";
    }

    @Override
    public ObjectInterface getNew() {
        return new QuestionObject();
    }
}
