package connection;

import object.ObjectInterface;

public interface ConnectionInterface {

    String url = "https://crest.stokoloko.dev:3000/api/";

    ObjectInterface get();
    ObjectInterface post();
    ObjectInterface get(String name);


}
