package it.stefanodp91.android.stripedemo;

/**
 * Created by stefanodp91 on 07/10/17.
 */

interface OnResponseRetrievedCallback<T> {
    void onResponseRetrievedSuccess(T response);

    void onResponseRetrievedError(Exception e);
}