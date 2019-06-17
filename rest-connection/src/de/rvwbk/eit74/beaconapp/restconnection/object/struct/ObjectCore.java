package de.rvwbk.eit74.beaconapp.restconnection.object.struct;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ObjectCore
 * Holds core operations for objects
 *
 * @author Niclas titius
 */
public abstract class ObjectCore implements ObjectInterface {


    private String id;
    private Map<String, String> data = new HashMap<String, String>();
    protected List<String> requiredKeys = new LinkedList<>();

    /**
     * Gets a data entry from data map
     *
     * @param key key of data entry
     * @return value of data entry
     */
    @Override
    public String get(String key) {
        if (this.data.containsKey(key)) {
            return this.data.get(key);
        }
        return null;
    }

    /**
     * Sets a data entry, alias for add(key,value)
     *
     * @param key   key of data entry
     * @param value value of data entry
     */
    @Override
    public void set(String key, String value) {
        this.add(key, value);
    }

    /**
     * Adds a data entry
     *
     * @param key   key of data entry
     * @param value value of data entry
     */
    @Override
    public void add(String key, String value) {
        this.data.put(key, value);
    }

    /**
     * Adds entire map to the data map
     *
     * @param m map to add
     */
    @Override
    public void add(Map m) {
        this.data.putAll(m);
    }

    /**
     * Getter for id
     *
     * @return id
     */
    @Override
    public String getId() {
        if (this.id == null) {
            this.id = this.data.get("id");
        }
        return id;
    }

    /**
     * Setter for id
     *
     * @param id id
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for data map
     *
     * @return data map
     */
    @Override
    public Map<String, String> getMap() {
        return data;
    }

    /**
     * Validates data map keys vs required keys
     *
     * @return valid object or not
     * @throws Exception invalid object
     */
    @Override
    public boolean validateKeys() throws Exception {
        if (requiredKeys.size() == 0) {
            throw new Exception("Class " + this.getClass().getName() + " has no required keys. This is invalid");
        }
        Map<String, String> lData = getMap();
        for (String key :
                requiredKeys) {
            if (!lData.containsKey(key)) {
                return true;
            }
        }
        return false;
    }
}
