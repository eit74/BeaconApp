package de.rvwbk.eit74.beaconapp.restconnection.connection.struct;

import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

import java.util.List;
import java.util.Map;

/**
 * @author Niclas Titius
 */
public interface ConnectionInterface {

    String url = "https://crest.stokoloko.dev:3000/api/";

    //Error codes
    final int INVALID_OBJECT = 1;
    final int INVALID_URL = 2;
    final int CONNECTION_ERROR = 3;
    final int CERFITICATE_ERROR = 4;
    final int UNIQUE_FIELD = 5;
    final int KEY_VALIDATION = 6;
    final int JSON_ERROR = 7;


    List<ObjectInterface> get(ObjectInterface objectInterface);

    String post(ObjectInterface objectInterface);

    List<ObjectInterface> get(ObjectInterface o, String filterKey, String filterValue);

    ObjectInterface get(ObjectInterface o, String id);

    String delete(ObjectInterface objectInterface, String id);

    Map<Integer, String> getLatestError();

    ObjectInterface put(ObjectInterface objectInterface);


}
