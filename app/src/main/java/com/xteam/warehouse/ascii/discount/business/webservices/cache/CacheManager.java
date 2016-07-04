package com.xteam.warehouse.ascii.discount.business.webservices.cache;

import android.support.annotation.NonNull;
import android.util.Log;

import com.xteam.warehouse.ascii.discount.AsciiWareHouseApplication;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.BaseResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Manager that handle the caching mechanism for the responses from the server.
 * The cache will be stored for one hour in the caching folder of the application.
 * <p/>
 * Created by Wraith on 7/4/2016.
 */
public class CacheManager {

    /**
     * Name of the file cache that will be stored into the cache directory of the application
     */
    private static final String CACHE_FILE_NAME = "ascii_warehouse_application_cache_file";

    /**
     * Tag string used to log messages from this class to Logcat
     */
    private static final String TAG = CacheManager.class.getName();

    /**
     * Single instance of this class
     */
    private static CacheManager sInstance;

    public static CacheManager getInstance() {
        if (sInstance == null) {
            sInstance = new CacheManager();
        }
        return sInstance;
    }

    private CacheManager() {
    }

    /**
     * Caches the serializable response to a file in the application's cache directory.
     *
     * @param response The serializable response that will be cached
     */
    public void cacheResponse(@NonNull BaseResponse response) {
        //// TODO: 7/4/2016 make this async to improve performance
        //Get a reference to the current application's cache directory
        File cacheDirectory = AsciiWareHouseApplication.getContext().getCacheDir();

        //Create a file in the current application's cache directory that will be used to store the object
        File cacheFile = new File(cacheDirectory.getAbsolutePath() + File.separator + CACHE_FILE_NAME);

        //Initialize streams and write the response to the cache file
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(cacheFile);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(response);
        } catch (IOException ioException) {
            Log.e(TAG, ioException.getMessage());
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException ioException) {
                Log.e(TAG, ioException.getMessage());
            }
        }
    }

    /**
     * Fetches cache response from the cache file, if such cached data exists.
     * If the data doesn't exist, null is returned
     */
    public BaseResponse fetchCacheResponse() {
        //// TODO: 7/4/2016 make this async to improve performance
        //Get a reference to the current application's cache directory
        File cacheDirectory = AsciiWareHouseApplication.getContext().getCacheDir();

        //Create a file in the current application's cache directory that will be used to store the object
        File cacheFile = new File(cacheDirectory.getAbsolutePath() + File.separator + CACHE_FILE_NAME);

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        BaseResponse response = null;
        try {
            fileInputStream = new FileInputStream(cacheFile);
            objectInputStream = new ObjectInputStream(fileInputStream);

            //Read the serializable object from the cache file
            response = (BaseResponse) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException exception) {
            Log.e(TAG, exception.getMessage());
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
        return response;
    }


}
