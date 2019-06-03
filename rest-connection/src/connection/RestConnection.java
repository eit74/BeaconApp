package connection;

import connection.struct.ConnectionInterface;
import object.struct.ObjectInterface;
import org.json.JSONArray;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RestConnection implements ConnectionInterface {


    private Map<Integer,String> latestError = new HashMap<Integer,String>();

    //SSL Certificate for crest.stokoloko.dev
    //@TODO: Autopull?
    private String cert = "-----BEGIN CERTIFICATE-----\n" +
            "MIIFXTCCBEWgAwIBAgISAzfqgJmChES/kw90pwvXma3rMA0GCSqGSIb3DQEBCwUA\n" +
            "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD\n" +
            "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0xOTA2MDExNDMzMzdaFw0x\n" +
            "OTA4MzAxNDMzMzdaMB4xHDAaBgNVBAMTE2NyZXN0LnN0b2tvbG9rby5kZXYwggEi\n" +
            "MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDchwT53+oIj/kv7ZyqHXatyNWj\n" +
            "NPESMCVu4WqPRc1aUTaZ+3Xtl/ODiM0dM3vlZiJTX+UqeP32tIbTia+3SVtEkPJ6\n" +
            "gUKqX7jL0N27MOg5+zQA7WIySBPeJw6POPsya/8GuACG7wVwc6fIHALkF8mR1tBR\n" +
            "LLDOBn39+adPMmTcktt361fjU8HT7Y3heQe9Pt4lLl2vlqcEqF81oI+QaIpCsgqM\n" +
            "eoDOmHQkFQDkQGtBhWTgR3rDUZ4WxKFkWMg2q66lr7+/3IUKUMzLvYAs6dyLTcPk\n" +
            "cQlCneo9fPI7pryLzuz4jHLL+h9KXSi+aRJ4p+Ss/3sZFxZiudm9YUHcf4rFAgMB\n" +
            "AAGjggJnMIICYzAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEG\n" +
            "CCsGAQUFBwMCMAwGA1UdEwEB/wQCMAAwHQYDVR0OBBYEFCzGsgfcFBcY4D+HGA8+\n" +
            "XtWjvxlHMB8GA1UdIwQYMBaAFKhKamMEfd265tE5t6ZFZe/zqOyhMG8GCCsGAQUF\n" +
            "BwEBBGMwYTAuBggrBgEFBQcwAYYiaHR0cDovL29jc3AuaW50LXgzLmxldHNlbmNy\n" +
            "eXB0Lm9yZzAvBggrBgEFBQcwAoYjaHR0cDovL2NlcnQuaW50LXgzLmxldHNlbmNy\n" +
            "eXB0Lm9yZy8wHgYDVR0RBBcwFYITY3Jlc3Quc3Rva29sb2tvLmRldjBMBgNVHSAE\n" +
            "RTBDMAgGBmeBDAECATA3BgsrBgEEAYLfEwEBATAoMCYGCCsGAQUFBwIBFhpodHRw\n" +
            "Oi8vY3BzLmxldHNlbmNyeXB0Lm9yZzCCAQMGCisGAQQB1nkCBAIEgfQEgfEA7wB2\n" +
            "AOJpS64m6OlACeiGG7Y7g9Q+5/50iPukjyiTAZ3d8dv+AAABaxOsw+MAAAQDAEcw\n" +
            "RQIgT4vwZmOXpGZMEkOa1SEJljQ5dwih3GcQ413PBpmDPKsCIQD4lIk26zmcd7gJ\n" +
            "mLK6F/eRQ1HIwdArxv9hUu9ovUueDQB1AGPy283oO8wszwtyhCdXazOkjWF3j711\n" +
            "pjixx2hUS9iNAAABaxOsxAcAAAQDAEYwRAIgHuJYzr38IuHY/89qyDjG81WkNC5T\n" +
            "gtQt5iKjG1dklGICICTsYINRlpakA4Ln3laU0Zlpo155tei8/nj9HVx8Ha0pMA0G\n" +
            "CSqGSIb3DQEBCwUAA4IBAQASdVA82o0NPZJ+QgXSH0pReEe48E5oCcwC5R5rvtV7\n" +
            "j0ccKMxsyicswzkI4celigg+d7sq9g0n81GpzlS2MoL3Zdz9duDkYWWpJlz5qiKs\n" +
            "WLvxkocoDZ1QbAxpk5dZ6bZ1J/RmuPU6woLvFS5aQkQdsNrAptIH8d+ZN7fShsbz\n" +
            "q4j7SHaKvpIGVQ2LzcoKry4nXDYiSW1oPy2SuS8qL4fJ+WO9VN09czKcweqCTLpE\n" +
            "h7XfShxepro7SDN5tqdv42d890I4A8mA+PjPFZ4Awjm8z/f1QfQz3bl+SQ/U5QP9\n" +
            "4JslMosrrHaXNpGm2QSlw8jQMAaT9+4Rv4SVkoFtBfUZ\n" +
            "-----END CERTIFICATE-----";


    private String request(ObjectInterface requestedObject,String type,String urlParam,String post) {
        this.latestError = new HashMap<Integer,String>();
        String myUrl = "";
        HttpsURLConnection con;
        SSLContext context;
        try {
            ////////////
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new ByteArrayInputStream(this.cert.getBytes());
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

            myUrl = this.url+requestedObject.getObjectString();
            if (!urlParam.equals("")){
                myUrl = myUrl+urlParam;
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
                while ((line = br.readLine()) != null){
                    stringBuilder.append(line);
                }
                con.disconnect();
                return stringBuilder.toString();
            } else if(code == 201){
                con.disconnect();
                return "201";
            } else if(code == 422) {
                InputStream responseBody = con.getErrorStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(responseBodyReader);
                while ((line = br.readLine()) != null){
                    stringBuilder.append(line);
                }
                con.disconnect();
                if (stringBuilder.toString().contains("is not unique")) {
                    this.latestError.put(UNIQUE_FIELD,"Unique field is not unique");
                    return null;

                }
                this.latestError.put(INVALID_OBJECT,"Invalid object to process");
                return null;
            }else {
                con.disconnect();
                return null;
            }
        } catch (MalformedURLException mue) {
            this.latestError.put(INVALID_URL,"Could not generate proper url: "+myUrl);
            return null;
        } catch (IOException ioe) {
            this.latestError.put(CONNECTION_ERROR,"Could not esablish connection to:"+myUrl);
            return null;
        } catch (CertificateException e) {
            this.latestError.put(CERFITICATE_ERROR,"Could not obtain certificate for:"+myUrl);
            return null;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            this.latestError.put(CERFITICATE_ERROR,"Failed to setup keystorage for ssl!");
            return null;
        }
    }
    @Override
    public List<ObjectInterface> get(ObjectInterface o, String filterKey, String filterValue) {
        List<ObjectInterface> ret = new LinkedList<>();
        String filter;
        if (!filterKey.equals("") && !filterValue.equals("")) {
            filter = "?filter={\"where\":{\""+filterKey+"\":\""+filterValue+"\"}}";
        }else{
            filter = "";
        }
        String retValue = this.request(o,"GET",filter,"");
        if (retValue == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray(retValue);
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectInterface oi = o.getNew();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            oi.add(jsonObject.toMap());
            ret.add(oi);
        }
        return ret;
    }

    @Override
    public List<ObjectInterface> get(ObjectInterface objectInterface) {
        return this.get(objectInterface,"","");
    }

    @Override
    public String post(ObjectInterface o) {
        try {
            if (o.validateKeys()) {
                this.latestError.put(INVALID_OBJECT,"Object has not all required keys");
                return null;
            }
        } catch (Exception e) {
            this.latestError.put(KEY_VALIDATION,e.getMessage());
            return null;
        }

        JSONObject js = new JSONObject(o.getMap());
        String retValue = this.request(o,"POST","",js.toString());
        if (retValue == null){
            return null;
        }
        return new JSONObject(retValue).get("id").toString();
    }

    @Override
    public ObjectInterface get(ObjectInterface o,String id) {
        String retValue = this.request(o,"GET","/"+id,"");
        if (retValue == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(retValue);
        ObjectInterface oi = o.getNew();
        oi.add(jsonObject.toMap());
        return oi;
    }

    @Override
    public String delete(ObjectInterface objectInterface, String id) {
        String retValue = this.request(objectInterface,"DELETE","/"+id,"");
        if (retValue == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(retValue);
        return jsonObject.get("count").toString();
    }

    @Override
    public Map<Integer,String> getLatestError() {
        return this.latestError;
    }

    @Override
    public ObjectInterface put(ObjectInterface objectInterface) {
        try {
        if (objectInterface.validateKeys() || objectInterface.getId() == null) {
            this.latestError.put(INVALID_OBJECT,"Object has not all required keys");
            return null;
        }
        } catch (Exception e) {
            this.latestError.put(KEY_VALIDATION,e.getMessage());
            return null;
        }
        JSONObject js = new JSONObject(objectInterface.getMap());
        String retValue = this.request(objectInterface,"PUT","/"+objectInterface.getId(),js.toString());
        if (retValue == null) {
            return null;
        }
        JSONObject jsRet = new JSONObject(retValue);
        ObjectInterface oi = objectInterface.getNew();
        oi.add(jsRet.toMap());
        return oi;
    }

}
