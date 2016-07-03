package com.xteam.warehouse.ascii.discount.business.webservices;

import com.xteam.warehouse.ascii.discount.business.webservices.responses.BaseResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Interface used to describe the endpoints using special Retrofit annotations to encode details
 * about the calls and parameters passed to the request methods.
 * <p>
 * <p>
 * Created by Wraith on 7/2/2016.
 */

interface AsciiSearchServiceInterface {

    /**
     * Base URL to the server where the calls are made
     */
    String BASE_URL = "http://74.50.59.155:5000";


    @GET("/api/search")
    Call<BaseResponse> fetchProducts();

    @GET("/api/search")
    Call<List<BaseResponse>> fetchProducts(@QueryMap Map<String, String> parameters);
}
