import connection.RestConnection;
import object.BeaconObject;
import object.PlayerObject;

public class Test {

    public static void main(String[] args) {
        RestConnection rc = new RestConnection();
        BeaconObject bo = new BeaconObject();
        bo.set("name","Beacon1");
        System.out.println(rc.post(bo));
    }

}
