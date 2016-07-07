package com.xteam.warehouse.ascii.discount;

import android.app.Application;
import android.content.Context;

/**
 * Application class to provide various application access features like {@link #getPackageName()}, {@link #getApplicationContext()}.
 * Created by cotuna on 7/4/16.
 */

public class AsciiWareHouseApplication extends Application {

    /**
     * Application level context used when activity context can't be reached and to avoid leaking whole views
     */
    private static Context sApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.sApplicationContext = getApplicationContext();
    }


    /**
     * Return a reference to the application context
     */
    public static Context getContext() {
        return sApplicationContext;
    }
}
