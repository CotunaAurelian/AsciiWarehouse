package com.xteam.warehouse.ascii.discount.business.datahandling;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xteam.warehouse.ascii.discount.business.datastorage.SharedPreferenceManager;
import com.xteam.warehouse.ascii.discount.business.webservices.AsciiSearchService;
import com.xteam.warehouse.ascii.discount.business.webservices.converters.ConverterUtil;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.BaseResponse;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.DataFetchListener;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.NDJsonResponse;
import com.xteam.warehouse.ascii.discount.model.dto.AsciiProductDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * This manager is used to provide an abstraction layer over the data handled across the application.
 * It will handle the data requests received from the UI and determine the way the data is fetched or stored if necessary.
 * <p>
 * There will be only one instance of this class available across the application, and t can be obtained
 * via the {@link #getInstance()}
 * <p>
 * Created by Wraith on 7/2/2016.
 */
public class DataManager {

    /**
     * Single instance of this class used to provide the needed functionalities
     */
    private static DataManager sInstance;

    /**
     * Returns the single instance of this class. If the instance is not created, it will be created
     * and a reference to it will be provided.
     */
    public static synchronized DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }


    /**
     * Private constructor declared in order to avoid any illegal instance creation. This class can have
     * only one instance, and it can be created only using {@link #getInstance()} method
     */
    private DataManager() {
    }

    /**
     * Fetches the data for the tags provided.
     *
     * @param tags The query of the data. It can be null. If this parameter is null, then the call will
     * still be triggered, but with default number of results to be returned
     * @param listener The listener that will be notified of any data fetching events
     */
    public void fetchData(@Nullable List<String> tags, @NonNull final ProductsSearchListener listener) {
        //TODO: implement the caching mechanism also.

        AsciiSearchService.getInstance().fetchData(tags, new DataFetchListener() {
            @Override
            public void onSuccess(@NonNull BaseResponse response) {
                if (response.getResponseType() == BaseResponse.NDJSON_RESPONSE) {
                    listener.onSuccess(ConverterUtil.convertToAsciiProductDTO((NDJsonResponse) response));
                } else {
                    //If we received anything but a NDJSON response, but no error, return an empty list
                    listener.onSuccess(new ArrayList<AsciiProductDTO>());
                }
            }

            @Override
            public void onError(@Nullable Throwable throwable) {
                listener.onError(throwable);
            }
        }, SharedPreferenceManager.getInstance().isOnlyInStockChecked());
    }
}
