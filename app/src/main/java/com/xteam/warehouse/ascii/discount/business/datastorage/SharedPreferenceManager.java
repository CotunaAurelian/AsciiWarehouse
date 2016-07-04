package com.xteam.warehouse.ascii.discount.business.datastorage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xteam.warehouse.ascii.discount.AsciiWareHouseApplication;

/**
 * Manager for share preference access
 * Created by cotuna on 7/4/16.
 */

public class SharedPreferenceManager {

    /**
     * Key for the onlyInStock user preferred option selection
     */
    private static final String PREF_ONLY_IN_STOCK_KEY = "pref.key.only.in.stock";

    /**
     * Single instance of this class.
     */
    private static SharedPreferenceManager sInstance;


    private SharedPreferenceManager() {
    }


    /**
     * Return a reference to the manager used to access the shared preferences context
     *
     * @return
     */
    public static SharedPreferenceManager getInstance() {
        if (sInstance == null) {
            sInstance = new SharedPreferenceManager();
        }
        return sInstance;
    }


    /**
     * Returns the onlyInStock user preference selection.
     * By default, if the user did not check anything, returns false
     */
    public boolean isOnlyInStockChecked() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AsciiWareHouseApplication.getContext());
        return preferences.getBoolean(PREF_ONLY_IN_STOCK_KEY, false);
    }

    /**
     * Sets the onlyInStock user preference selection
     */
    public void setOnlyInStockChecked(boolean checked) {
        //formatter:off
        PreferenceManager.getDefaultSharedPreferences(AsciiWareHouseApplication.getContext()).
                        edit().
                        putBoolean(PREF_ONLY_IN_STOCK_KEY, checked).
                        apply();
        //formatter:on
    }


}
