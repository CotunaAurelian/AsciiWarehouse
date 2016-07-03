package com.xteam.warehouse.ascii.discount.business.datahandling;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xteam.warehouse.ascii.discount.business.webservices.responses.BaseResponse;

/**
 * Interface definition for callbacks to be invoked when data is received.
 * <p>
 * Created by Wraith on 7/2/2016.
 */

public interface DataFetchListener {

    /**
     * Called when the data has been successfully fetched
     * @param response The response data
     */
    void onSuccess(@NonNull BaseResponse response);

    /**
     * Called when there was something wrong with the request or response
     * @param exception The throwable that causes the call to fail. This parameter might be null
     */
    void onError(@Nullable Throwable exception);

}
