package de.rvwbk.eit74.beaconapp.restconnection.object.struct;

import java.util.Map;

public interface ObjectInterface {

    /**
     * Retunrs object name as string for url build
     *
     * @return object name
     */
    String getObjectString();

    String get(String key);

    /**
     * Returns a new instance of this object
     *
     * @return new instance of this object
     */
    ObjectInterface getNew();

    void add(String key, String value);

    void add(Map m);

    String getId();

    void setId(String id);

    Map<String, String> getMap();

    boolean validateKeys() throws Exception;

    void set(String key, String value);

}
