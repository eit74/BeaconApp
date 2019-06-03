package connection.struct;

import object.struct.ObjectInterface;

import java.util.List;
import java.util.Map;

public interface ConnectionInterface {

    String url = "https://crest.stokoloko.dev:3000/api/";
    final int INVALID_OBJECT    = 1;
    final int INVALID_URL       = 2;
    final int CONNECTION_ERROR  = 3;
    final int CERFITICATE_ERROR = 4;
    final int UNIQUE_FIELD      = 5;
    final int KEY_VALIDATION    = 6;


    List<ObjectInterface> get(ObjectInterface objectInterface);
    String post(ObjectInterface objectInterface);
    List<ObjectInterface> get(ObjectInterface o,String filterKey, String filterValue);
    ObjectInterface get(ObjectInterface o,String id);
    String delete(ObjectInterface objectInterface, String id);
    Map<Integer,String> getLatestError();
    ObjectInterface put(ObjectInterface objectInterface);


}
