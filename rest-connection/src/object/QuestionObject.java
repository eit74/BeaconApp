package object;

import object.struct.ObjectCore;
import object.struct.ObjectInterface;

public class QuestionObject extends ObjectCore implements ObjectInterface {

    public QuestionObject() {
        this.requiredKeys.add("question");
        this.requiredKeys.add("answer1");
        this.requiredKeys.add("answer2");
        this.requiredKeys.add("correctAnswer");
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
