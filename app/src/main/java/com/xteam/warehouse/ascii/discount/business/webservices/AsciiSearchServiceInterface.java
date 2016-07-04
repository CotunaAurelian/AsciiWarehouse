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


    /**
     * String name for the onlyInStock call parameter. Setting this parameter, will make the call return only products that are in stock
     */
    String ONLY_IN_STOCK_PARAMETER = "onlyInStock";

    /**
     * String name for the limit call parameter. This parameter is used to specify the number of the products returned by the call.
     */
    String LIMIT_PARAMETER = "limit";

    /**
     * String name for the skip call parameter. This parameter is used to specify the number of items to be skipped by the call
     */
    String SKIP_PARAMETER = "skip";

    /**
     * String name for the query call parameter. This parameter is used to specify tags used for the search parameters
     */
    String QUERY_PARAMTER = "q";


    @GET("/api/search")
    Call<List<BaseResponse>> fetchProducts(@QueryMap Map<String, String> parameters);
}
