package object.struct;

import java.util.Map;

public interface ObjectInterface {

    String getObjectString();
    String get(String key);
    ObjectInterface getNew();
    void add(String key, String value);
    void add(Map m);
    String getId();
    void setId(String id);
    Map<String,String> getMap();
    boolean validateKeys() throws Exception;
    void set(String key, String value);

}
