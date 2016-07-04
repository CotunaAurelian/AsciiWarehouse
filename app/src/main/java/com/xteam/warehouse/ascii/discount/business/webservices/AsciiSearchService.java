package com.xteam.warehouse.ascii.discount.business.webservices;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xteam.warehouse.ascii.discount.business.webservices.responses.DataFetchListener;
import com.xteam.warehouse.ascii.discount.business.webservices.converters.NDJsonConverter;
import com.xteam.warehouse.ascii.discount.business.webservices.converters.StringFormatUtils;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Manages the calls to the web server using Retrofit and OKHttp related classes.
 * All the Ascii Discount Warehouse search operations are handled by this api service.
 * Because there is no need for this class to have multiple instances, the calls can be made
 * accessing the single instance of this class using {@link #getInstance()} method
 * <p>
 * Created by Wraith on 7/2/2016.
 */

public class AsciiSearchService {
    /**
     * Tag used to identify any logging made in this class
     */
    private static final String TAG = AsciiSearchService.class.getName();


    /**
     * Single instance of this class.
     */
    private static AsciiSearchService sInstance;

    /**
     * Private constructor to prevent any outside of class instance creation
     */
    private AsciiSearchService() {
    }

    /**
     * Returns a single instance of this class. If there are no available instances, a new one is created
     * and returned
     */
    public static AsciiSearchService getInstance() {
        if (sInstance == null) {
            sInstance = new AsciiSearchService();
        }
        return sInstance;
    }

    /**
     * Fetches the data for the tags provided.
     *
     * @param tags The query of the data. It can be null. If this parameter is null, then the call will
     * still be triggered, but with default number of results to be returned
     * @param listener The listener that will be notified of any data fetching events
     * @param onlyInStock Flag set if only in stock products have to be downloaded or not
     */
    public void fetchData(@Nullable List<String> tags, @NonNull final DataFetchListener listener, boolean onlyInStock) {

        //formatter:off
        //Prepare the retrofit object and add all the necessary parameters to it through the builder
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AsciiSearchServiceInterface.BASE_URL).addConverterFactory(
                        NDJsonConverter.newInstance()).build();

        //Create the api search access object based on the already defined AsciiSearchServiceInterface
        AsciiSearchServiceInterface apiSearchAccess = retrofit.create(AsciiSearchServiceInterface.class);

        //Make the async calls
        Map<String, String> parameters = new HashMap<>();
        //We are going to return only products that are in stock
        parameters.put(AsciiSearchServiceInterface.ONLY_IN_STOCK_PARAMETER, onlyInStock ? "1" : "0");
        //TODO: Implement a functionality that calculates how many items fit the screen
        parameters.put(AsciiSearchServiceInterface.LIMIT_PARAMETER, "40");
        //TODO: Implement a functionality that calculates how many items should be skipped
        parameters.put(AsciiSearchServiceInterface.SKIP_PARAMETER, "0");
        if (tags != null) {
            parameters.put(AsciiSearchServiceInterface.QUERY_PARAMTER, StringFormatUtils.appendStringsWithDelimiter(tags, null));
        }


        Call<List<BaseResponse>> call = apiSearchAccess.fetchProducts(parameters);
        call.enqueue(new Callback<List<BaseResponse>>() {
            @Override
            public void onResponse(Call<List<BaseResponse>> call, Response<List<BaseResponse>> response) {
                listener.onSuccess((BaseResponse) response.body());
            }

            @Override
            public void onFailure(Call<List<BaseResponse>> call, Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
                listener.onError(throwable);
            }
        });
        //formatter:on


    }


}
