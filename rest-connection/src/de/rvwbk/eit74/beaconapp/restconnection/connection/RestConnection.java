package de.rvwbk.eit74.beaconapp.restconnection.connection;

import de.rvwbk.eit74.beaconapp.restconnection.connection.struct.ConnectionInterface;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.*;

/**
 * RestConnection
 * Handles the connection to the loopback rest api
 *
 * @author Niclas titius
 */
public class RestConnection implements ConnectionInterface {


    private Map<Integer, String> latestError = new HashMap<Integer, String>();

    private SSLContext context;

    /**
     * Performs HTTPS request
     *
     * @param requestedObject Instance of object to perform the request on
     * @param type            Type of request e.g. GET,POST etc
     * @param urlParam        Appendable url parameter
     * @param post            Post content of the request
     * @return json
     */
    private String request(ObjectInterface requestedObject, String type, String urlParam, String post) {
        this.latestError = new HashMap<Integer, String>();
        String myUrl = "";
        HttpsURLConnection con;
        try {
            this.buildCertificate();
            myUrl = this.url + requestedObject.getObjectString();
            if (!urlParam.equals("")) {
                myUrl = myUrl + urlParam;
            }
            URL url = new URL(myUrl);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod(type);
            con.setSSLSocketFactory(context.getSocketFactory());
            con.setRequestProperty("User-Agent", "PlayerRestHandler");
            con.setRequestProperty("Content-Type", "application/json");

            if (!post.equals("")) {
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(post.getBytes());
                os.flush();
                os.close();
            }
            int code = con.getResponseCode();
            if (code == 200) {
                InputStream responseBody = con.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(responseBodyReader);
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
                con.disconnect();
                return stringBuilder.toString();
            } else if (code == 201) {
                con.disconnect();
                return "201";
            } else if (code == 422) {
                InputStream responseBody = con.getErrorStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(responseBodyReader);
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
                con.disconnect();
                if (stringBuilder.toString().contains("is not unique")) {
                    this.latestError.put(UNIQUE_FIELD, "Unique field is not unique");
                    return null;

                }
                this.latestError.put(INVALID_OBJECT, "Invalid object to process");
                return null;
            } else {
                con.disconnect();
                return null;
            }
        } catch (MalformedURLException mue) {
            this.latestError.put(INVALID_URL, "Could not generate proper url: " + myUrl);
            return null;
        } catch (IOException ioe) {
            this.latestError.put(CONNECTION_ERROR, "Could not esablish connection to:" + myUrl);
            return null;
        } catch (CertificateException e) {
            this.latestError.put(CERFITICATE_ERROR, "Could not obtain certificate for:" + myUrl);
            return null;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            this.latestError.put(CERFITICATE_ERROR, "Failed to setup keystorage for ssl!");
            return null;
        }
    }

    /**
     * Gets a list object by filtering
     *
     * @param o           Instance of object to perform the request on
     * @param filterKey   key to filter by, may be empty for no search
     * @param filterValue value to filter by, may be empty for no search
     * @return search results
     */
    @Override
    public List<ObjectInterface> get(ObjectInterface o, String filterKey, String filterValue) {
        List<ObjectInterface> ret = new LinkedList<>();
        String filter;
        if (!filterKey.equals("") && !filterValue.equals("")) {
            filter = "?filter={\"where\":{\"" + filterKey + "\":\"" + filterValue + "\"}}";
        } else {
            filter = "";
        }
        String retValue = this.request(o, "GET", filter, "");
        if (retValue == null) {
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(retValue);
            for (int i = 0; i < jsonArray.length(); i++) {
                ObjectInterface oi = o.getNew();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                oi.add(this.JsonObjectToMap(jsonObject));
                ret.add(oi);
            }
        } catch (JSONException je) {
            this.latestError.put(JSON_ERROR, je.getMessage());
            return null;
        }
        return ret;
    }

    /**
     * Gets a list of all objects, alias for get(objectInterface,"","")
     *
     * @param objectInterface Instance of object to perform the request on
     * @return all objects
     */
    @Override
    public List<ObjectInterface> get(ObjectInterface objectInterface) {
        return this.get(objectInterface, "", "");
    }

    /**
     * Posts a object to the rest api to create it
     *
     * @param o Filled instance of object to perform the request on
     * @return id of new object
     */
    @Override
    public String post(ObjectInterface o) {
        try {
            if (o.validateKeys()) {
                this.latestError.put(INVALID_OBJECT, "Object has not all required keys");
                return null;
            }
        } catch (Exception e) {
            this.latestError.put(KEY_VALIDATION, e.getMessage());
            return null;
        }
        try {
            JSONObject js = new JSONObject(o.getMap());
            String retValue = this.request(o, "POST", "", js.toString());
            if (retValue == null) {
                return null;
            }
            return new JSONObject(retValue).get("id").toString();
        } catch (JSONException je) {
            this.latestError.put(JSON_ERROR, je.getMessage());
            return null;
        }
    }

    /**
     * Gets a specific object
     *
     * @param o  Instance of object to perform the request on
     * @param id id of the specific object
     * @return found object
     */
    @Override
    public ObjectInterface get(ObjectInterface o, String id) {
        String retValue = this.request(o, "GET", "/" + id, "");
        if (retValue == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(retValue);
            ObjectInterface oi = o.getNew();
            oi.add(this.JsonObjectToMap(jsonObject));
            return oi;
        } catch (JSONException je) {
            this.latestError.put(JSON_ERROR, je.getMessage());
            return null;
        }
    }

    /**
     * Deletes object from rest api
     *
     * @param objectInterface Instance of object to delete
     * @param id              id of object to delete
     * @return number of affected entries
     */
    @Override
    public String delete(ObjectInterface objectInterface, String id) {
        String retValue = this.request(objectInterface, "DELETE", "/" + id, "");
        if (retValue == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(retValue);
            return jsonObject.get("count").toString();
        } catch (JSONException je) {
            this.latestError.put(JSON_ERROR, je.getMessage());
            return null;
        }
    }

    /**
     * Returns a list of the error of the last request
     * See ConnectionInterface for error ids
     *
     * @return error map
     */
    @Override
    public Map<Integer, String> getLatestError() {
        return this.latestError;
    }

    /**
     * Puts a object into the rest api, will cause an update of the object
     *
     * @param objectInterface Filled instance of object to perform the action on
     * @return updated object
     */
    @Override
    public ObjectInterface put(ObjectInterface objectInterface) {
        try {
            if (objectInterface.validateKeys() || objectInterface.getId() == null) {
                this.latestError.put(INVALID_OBJECT, "Object has not all required keys");
                return null;
            }
        } catch (Exception e) {
            this.latestError.put(KEY_VALIDATION, e.getMessage());
            return null;
        }
        try {
            JSONObject js = new JSONObject(objectInterface.getMap());
            String retValue = this.request(objectInterface, "PUT", "/" + objectInterface.getId(), js.toString());
            if (retValue == null) {
                return null;
            }
            JSONObject jsRet = new JSONObject(retValue);
            ObjectInterface oi = objectInterface.getNew();
            oi.add(this.JsonObjectToMap(jsRet));
            return oi;
        } catch (JSONException je) {
            this.latestError.put(JSON_ERROR, je.getMessage());
            return null;
        }
    }

    /**
     * Sets up the cerfiticate for crest.stokoloko.dev and adds it as trusted
     *
     * @throws CertificateException     Problem with certificate
     * @throws IOException              Problem with accessing files
     * @throws KeyStoreException        Problem with key
     * @throws NoSuchAlgorithmException Problem with key
     * @throws KeyManagementException   Problem with key
     */
    private void buildCertificate() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String url = "http://cert.stokoloko.dev/crest/cert.pem";
        StringBuilder builder = new StringBuilder();
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
            //FileOutputStream fileOutputStream = new FileOutputStream();
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                builder.append(new String(dataBuffer, 0, bytesRead));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // handle exception
        }

        // Load CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = new ByteArrayInputStream(builder.toString().getBytes());
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
    }

    /**
     * Converts a JsonObject to map
     * @param jsonObject JsonObject to convert
     * @return map of converted jsonObject
     */
    private Map<String, String> JsonObjectToMap(JSONObject jsonObject) {
        try {
            Iterator<String> keys = jsonObject.keys();
            Map<String, String> retMap = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                retMap.put(key, jsonObject.get(key).toString());
            }
            return retMap;
        } catch (JSONException je) {
            this.latestError.put(JSON_ERROR, je.getMessage());
            return null;
        }
    }

}
