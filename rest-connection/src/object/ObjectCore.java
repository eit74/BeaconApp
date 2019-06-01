package object;

import java.util.HashMap;
import java.util.Map;

public abstract class ObjectCore {

    private Map<String,String> data = new HashMap<String, String>();

    public String get(String key) {
        if (this.data.containsKey(key)){
            return this.data.get(key);
        }
        return null;
    }

    public void add(String key, String value) {
        this.data.put(key,value);
    }


}
