package com.xteam.warehouse.ascii.discount.business.webservices;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xteam.warehouse.ascii.discount.business.webservices.cache.CacheManager;
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
     * @param bypassCache Flag set if the cache should be ignored. If set to true, the call is made to the server, even if there is a valid cache
     * with data.
     * @param limit - The limit of the items to be queried from the server
     * @param skip - The number of items to be skipped when creating a request
     */
    public void fetchData(@Nullable final List<String> tags, @NonNull final DataFetchListener listener, final boolean onlyInStock,
                    boolean bypassCache, final int limit, final int skip) {

        Log.d(TAG, "Cache status: Valid - " + CacheManager.getInstance().isCacheValid());
        if (bypassCache || !CacheManager.getInstance().isCacheValid()) {
            fetchData(tags, listener, onlyInStock, limit, skip);
        } else {
            //Read the data from cache
            CacheManager.getInstance().fetchCacheResponse(new DataFetchListener() {
                @Override
                public void onSuccess(@NonNull BaseResponse response) {
                    Log.d(TAG, "Data returned from cache");
                    listener.onSuccess(response);
                }

                @Override
                public void onError(@Nullable Throwable exception) {
                    Log.d(TAG, "Something wrong with the cache, continue and fetch data from server");
                    //If there was an error loading the data from cache, just let the flow continue and request the data from server
                    fetchData(tags, listener, onlyInStock, limit, skip);
                }
            });
        }
    }

    /**
     * Fetches the data for the tags provided.
     *
     * @param tags The query of the data. It can be null. If this parameter is null, then the call will
     * still be triggered, but with default number of results to be returned
     * @param listener The listener that will be notified of any data fetching events
     * @param onlyInStock Flag set if only in stock products have to be downloaded or not
     * @param limit The limit of the items to be queried from the server
     * @param skip The number of items to be skipped when creating a request
     */
    private void fetchData(@Nullable List<String> tags, @NonNull final DataFetchListener listener, boolean onlyInStock, int limit, int skip) {
        Log.d(TAG, "Cache status: Valid - " + CacheManager.getInstance().isCacheValid());

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
        parameters.put(AsciiSearchServiceInterface.LIMIT_PARAMETER, String.valueOf(limit));
        parameters.put(AsciiSearchServiceInterface.SKIP_PARAMETER, String.valueOf(skip));
        if (tags != null) {
            parameters.put(AsciiSearchServiceInterface.QUERY_PARAMTER, StringFormatUtils.appendStringsWithDelimiter(tags, null));
        }


        Call<List<BaseResponse>> call = apiSearchAccess.fetchProducts(parameters);
        call.enqueue(new Callback<List<BaseResponse>>() {
            @Override
            public void onResponse(Call<List<BaseResponse>> call, Response<List<BaseResponse>> response) {
                listener.onSuccess((BaseResponse) response.body());

                //Store the response to cache
                CacheManager.getInstance().cacheResponse((BaseResponse) response.body());
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
