package connection;

import object.ObjectInterface;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;

public abstract class ConnectionCore implements ConnectionInterface {


    String cert = "-----BEGIN CERTIFICATE-----\n" +
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


    String request(ObjectInterface requestedObject,String type) {
        String myUrl = "";
        HttpsURLConnection con;
        try {
            ////////////
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new ByteArrayInputStream(this.cert.getBytes());
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
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
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);


            myUrl = this.url+requestedObject.getObjectString();
            URL url = new URL(myUrl);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod(type);
            con.setSSLSocketFactory(context.getSocketFactory());
            con.setRequestProperty("User-Agent", "PlayerRestHandler");
            con.setRequestProperty("Content-Type", "application/json");
            int code = con.getResponseCode();
            if (code == 200) {
                InputStream responseBody = con.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(responseBodyReader);
                while ((line = br.readLine()) != null){
                    stringBuilder.append(line);
                }
                con.disconnect();
                return stringBuilder.toString();
            } else {
                con.disconnect();
                return null;
            }
        } catch (MalformedURLException mue) {
            System.out.println("Could not generate proper url: "+myUrl);
            return null;
        } catch (IOException ioe) {
            System.out.println("Could not esablish connection to:"+myUrl);
            return null;
        } catch (CertificateException e) {
            System.out.println("Could not obtain certificate for:"+myUrl);
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }

}
