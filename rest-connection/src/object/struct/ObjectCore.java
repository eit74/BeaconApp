package object.struct;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ObjectCore implements ObjectInterface {

    private String id;
    private Map<String, String> data = new HashMap<String, String>();
    protected List<String> requiredKeys = new LinkedList<>();


    @Override
    public String get(String key) {
        if (this.data.containsKey(key)) {
            return this.data.get(key);
        }
        return null;
    }

    @Override
    public void set(String key, String value){
        this.add(key,value);
    }

    @Override
    public void add(String key, String value) {
        this.data.put(key, value);
    }

    @Override
    public void add(Map m) {
        this.data.putAll(m);
    }

    @Override
    public String getId() {
        if (this.id == null) {
            this.id = this.data.get("id");
        }
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Map<String, String> getMap() {
        return data;
    }

    @Override
    public boolean validateKeys() throws Exception {
        if (requiredKeys.size() == 0) {
            throw new Exception("Class "+this.getClass().getName()+" has no required keys. This is invalid");
        }
        Map<String,String> lData = getMap();
        for (String key :
                requiredKeys) {
            if (!lData.containsKey(key)) {
                return true;
            }
        }
        return false;
    }
}
