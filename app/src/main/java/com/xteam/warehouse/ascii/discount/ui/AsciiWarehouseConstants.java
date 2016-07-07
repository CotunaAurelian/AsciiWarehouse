package com.xteam.warehouse.ascii.discount.ui;

/**
 * Interface for constants used across the application
 * Created by cotuna on 7/6/16.
 */

public interface AsciiWarehouseConstants {

    /**
     * Key used to pass products around in Bundles
     */
    String PRODUCT_BUNDLE_KEY = AsciiWarehouseConstants.class.getName() + "product.bundle.key";

    /**
     * Clicked used to pass a view x position from one screen to another. Usually this will be used to provide proper animations
     */
    String VIEW_X_POSITION = AsciiWarehouseConstants.class.getName() + "view.x.position.bundle.key";

    /**
     * Clicked used to pass a view x position from one screen to another. Usually this will be used to provide proper animations
     */
    String VIEW_Y_POSITION = AsciiWarehouseConstants.class.getName() + "view.y.position.bundle.key";
}
