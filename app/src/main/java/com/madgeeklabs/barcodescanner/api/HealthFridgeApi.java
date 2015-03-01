package com.madgeeklabs.barcodescanner.api;

import com.madgeeklabs.barcodescanner.models.Product;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by goofyahead on 2/28/15.
 */
public interface HealthFridgeApi {

    @POST("/lastProduct")
    void registerNumber(@Body Product user,
                        Callback<Response> cb);
}
