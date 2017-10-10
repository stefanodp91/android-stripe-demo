package it.stefanodp91.android.stripedemo;

/**
 * Created by stefanodp91 on 07/10/17.
 */

interface OnResponseRetrievedCallback {
    void onResponseRetrievedSuccess(Object response);

    void onResponseRetrievedError(Exception e);
}