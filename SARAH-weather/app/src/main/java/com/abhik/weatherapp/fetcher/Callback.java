package com.abhik.weatherapp.fetcher;

/**
 * Callback interface to implemented by fetchers.
 */

public interface Callback<T> {
    void onSuccess(T t);
    void onFailure();
}
