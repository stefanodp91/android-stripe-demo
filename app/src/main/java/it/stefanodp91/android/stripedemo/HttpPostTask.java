package it.stefanodp91.android.stripedemo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by stefanodp91 on 20/02/17.
 */
class HttpPostTask extends AsyncTask<Object, String, String> {
    private static final String TAG = HttpPostTask.class.getName();

    private InputStream inputStream;
    private HttpURLConnection urlConnection;
    private byte[] outputBytes;
    private String queryParams;
    private String responseData;
    private OnResponseRetrievedCallback callback;
    private String mAuth;
    private Exception mException;


    public HttpPostTask(String queryParams, OnResponseRetrievedCallback callback) {
        this.queryParams = queryParams;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Object... params) {

        // Send data
        try {

            // forming th java.net.URL object
            URL url = new URL(params[0].toString());
            urlConnection = (HttpURLConnection) url.openConnection();

            // set auth
            if (mAuth != null && !mAuth.isEmpty())
                urlConnection.setRequestProperty("Authorization", mAuth);

            // pass post data
            outputBytes = queryParams.getBytes("UTF-8");

            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);
            os.close();

            // Get Response and execute WebService request
            int statusCode = urlConnection.getResponseCode();

            // 200 represents HTTP OK
            if (statusCode == HttpsURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                responseData = convertStreamToString(inputStream);
            } else if (statusCode == HttpsURLConnection.HTTP_NOT_FOUND) {
                responseData = "server returns :" + HttpsURLConnection.HTTP_NOT_FOUND;
                mException = new Exception(responseData);
            } else if (statusCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                responseData = "server returns :" + HttpsURLConnection.HTTP_BAD_REQUEST;
                mException = new Exception(responseData);
            } else if (statusCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                responseData = "server returns :" + HttpsURLConnection.HTTP_FORBIDDEN;
                mException = new Exception(responseData);
            } else {
                mException = new Exception("generic exception");
            }

        } catch (Exception e) {
            mException = e;
            responseData = e.getMessage();
        }

        return responseData;
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            mException = e;
            responseData = e.getMessage();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    @Override
    protected void onPostExecute(String response) {
        Log.i(TAG, "onPostExecute");

        if (mException != null)
            callback.onResponseRetrievedError(mException);
        else
            callback.onResponseRetrievedSuccess(response);
    }


    public void setAuth(String auth) {
        mAuth = auth;
    }
}