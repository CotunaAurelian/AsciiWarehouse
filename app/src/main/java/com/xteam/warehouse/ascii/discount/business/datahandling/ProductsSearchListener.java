package com.xteam.warehouse.ascii.discount.business.datahandling;

import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xteam.warehouse.ascii.discount.model.dto.AsciiProductDTO;

/**
 * Interface definition for callbacks to be invoked when data is received. This interface will be used in the user interface layer.
 * It will handle product dto objectss that are wrapping the information that will be displayed on the UI
 * <p/>
 * Created by cotuna on 7/4/16.
 */

public interface ProductsSearchListener {

    /**
     * Called when the data has been successfully fetched
     *
     * @param asciiProductDTO List of received objects
     */
    void onSuccess(@NonNull List<AsciiProductDTO> asciiProductDTO);

    /**
     * Called when there was something wrong with the request or response
     *
     * @param exception The throwable that causes the call to fail. This parameter might be null
     */
    void onError(@Nullable Throwable exception);
}
