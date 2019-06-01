package connection;

import object.ObjectInterface;
import object.PlayerObject;
import org.json.JSONObject;

import java.io.StringReader;

public class PlayerConnection extends ConnectionCore implements ConnectionInterface {

    @Override
    public ObjectInterface get() {
        JSONObject jsonObject = new JSONObject(this.request(new PlayerObject(),"GET"));

        return null;
    }

    @Override
    public ObjectInterface post() {
        return null;
    }

    @Override
    public ObjectInterface get(String name) {
        return null;
    }
}
