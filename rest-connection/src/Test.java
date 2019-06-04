import connection.RestConnection;
import object.BeaconObject;

public class Test {

    public static void main(String[] args) {
        RestConnection rc = new RestConnection();
        BeaconObject bo = new BeaconObject();
        bo.set("name","Beacon2");
        System.out.println(rc.post(bo));
        System.out.println(rc.getLatestError().toString());
    }

}
