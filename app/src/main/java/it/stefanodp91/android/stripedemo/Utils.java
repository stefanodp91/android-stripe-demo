package it.stefanodp91.android.stripedemo;

/**
 * Created by stefanodp91 on 18/10/17.
 */

public class Utils {
    public static String getBase64BasicAuth(String username, String password) {

        String auth = username + ":" + password;
        String basicAuth = "Basic " + android.util.Base64
                .encodeToString(auth.getBytes(), android.util.Base64.NO_WRAP);

        return basicAuth;
    }
}
