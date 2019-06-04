package de.rvwbk.eit74.beaconapp.restconnection.connection;

import android.os.AsyncTask;

import java.util.List;
import java.util.Map;

import de.rvwbk.eit74.beaconapp.restconnection.connection.struct.ConnectionInterface;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

/**
 * Async Handler for RestConnection
 * Manages the async execution of the rest connection
 *
 * @author Niclas Titius
 */
public class AsyncConnection extends AsyncTask implements ConnectionInterface {

    private ObjectInterface inputObject = null;
    private ObjectInterface outputObject = null;
    private List<ObjectInterface> outputList = null;
    private String outputString = null;
    private String operation = null;
    private String searchKey = null;
    private String searchValue = null;
    private String objectKey = null;

    private RestConnection restConnection = null;

    /**
     * Performs async operation
     *
     * @param objects not needed
     * @return not needed
     */
    @Override
    protected Object doInBackground(Object[] objects) {
        restConnection = new RestConnection();
        switch (this.operation) {
            case "PUT":
                this.outputObject = restConnection.put(this.inputObject);
                break;
            case "POST":
                this.outputString = restConnection.post(this.inputObject);
                break;
            case "GET":
                if (this.objectKey != null) {
                    this.outputObject = restConnection.get(this.inputObject, this.objectKey);
                } else if (this.searchKey != null && this.searchValue != null) {
                    this.outputList = restConnection.get(this.inputObject, this.searchKey, this.searchValue);
                } else {
                    this.outputList = restConnection.get(this.inputObject);
                }
                break;
            case "DELETE":
                this.outputString = restConnection.delete(this.inputObject, this.objectKey);
                break;

        }
        return true;
    }

    /**
     * Forwarded async handler for RestConnection
     * see RestConnection doc
     *
     * @param objectInterface Instance of object to perform action on
     * @return results
     */
    @Override
    public List<ObjectInterface> get(ObjectInterface objectInterface) {
        this.operation = "GET";
        this.inputObject = objectInterface;
        try {
            this.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ObjectInterface> ret = this.outputList;
        this.flushVars();
        return ret;

    }

    /**
     * Forwarded async handler for RestConnection
     * see RestConnection doc
     *
     * @param objectInterface Instance of object to perform action on
     * @return result
     */
    @Override
    public String post(ObjectInterface objectInterface) {
        this.operation = "POST";
        this.inputObject = objectInterface;
        try {
            this.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ret = this.outputString;
        this.flushVars();
        return ret;
    }

    /**
     * Forwarded async handler for RestConnection
     * see RestConnection doc
     *
     * @param o           Instance of object to perform action on
     * @param filterKey   search key
     * @param filterValue search value
     * @return results
     */
    @Override
    public List<ObjectInterface> get(ObjectInterface o, String filterKey, String filterValue) {
        this.operation = "GET";
        this.inputObject = o;
        this.searchKey = filterKey;
        this.searchValue = filterValue;
        try {
            this.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ObjectInterface> ret = this.outputList;
        this.flushVars();
        return ret;
    }

    /**
     * Forwarded async handler for RestConnection
     * see RestConnection doc
     *
     * @param o  Instance of object to perform action on
     * @param id id of object
     * @return result
     */
    @Override
    public ObjectInterface get(ObjectInterface o, String id) {
        this.operation = "GET";
        this.inputObject = o;
        this.objectKey = id;
        try {
            this.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectInterface ret = this.outputObject;
        this.flushVars();
        return ret;
    }

    /**
     * Forwarded async handler for RestConnection
     * see RestConnection doc
     *
     * @param objectInterface Instance of object to perform action on
     * @param id              id of object
     * @return result
     */
    @Override
    public String delete(ObjectInterface objectInterface, String id) {
        this.operation = "DELETE";
        this.inputObject = objectInterface;
        this.objectKey = id;
        try {
            this.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ret = this.outputString;
        this.flushVars();
        return ret;
    }

    /**
     * Forwarded error handler
     *
     * @return list of errors
     */
    public Map<Integer, String> getLatestError() {
        if (this.restConnection == null) {
            return null;
        }
        return this.restConnection.getLatestError();
    }

    /**
     * Forwarded async handler for RestConnection
     * see RestConnection doc
     *
     * @param objectInterface Instance of object to perform action on
     * @return result
     */
    @Override
    public ObjectInterface put(ObjectInterface objectInterface) {
        this.operation = "PUT";
        this.inputObject = objectInterface;
        try {
            this.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectInterface ret = this.outputObject;
        this.flushVars();
        return ret;
    }

    /**
     * Flushes all variables to avoid missing overrides
     */
    private void flushVars() {
        this.inputObject = null;
        this.outputObject = null;
        this.outputList = null;
        this.outputString = null;
        this.operation = null;
        this.searchKey = null;
        this.searchValue = null;
        this.objectKey = null;

    }
}
