package com.xteam.warehouse.ascii.discount.business.webservices.converters;

import com.xteam.warehouse.ascii.discount.business.webservices.responses.BaseResponse;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.NDJsonResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Converter for the new line delimited JSON format
 * <p>
 * <p>
 * Created by Wraith on 7/2/2016.
 */

public class NDJsonConverter extends Converter.Factory implements Converter<ResponseBody, BaseResponse> {

    /**
     * Creates a new instance of this converter
     */
    public static NDJsonConverter newInstance() {
        return new NDJsonConverter();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new NDJsonConverter();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations,
                    Retrofit retrofit) {
        return null;
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return super.stringConverter(type, annotations, retrofit);
    }


    @Override
    public BaseResponse convert(ResponseBody value) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(value.byteStream()));
        StringBuilder responseStringBuilder = new StringBuilder();

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            responseStringBuilder.append(line);
        }

        //Close the buffers after we've done with them
        bufferedReader.close();
        value.close();

        if (responseStringBuilder.toString().trim().length() == 0) {
            //No Data received
            return null;
        }
        return new NDJsonResponse(responseStringBuilder.toString());
    }
}
