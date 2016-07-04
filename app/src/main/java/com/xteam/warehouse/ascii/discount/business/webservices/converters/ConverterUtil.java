package com.xteam.warehouse.ascii.discount.business.webservices.converters;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.NDJsonResponse;
import com.xteam.warehouse.ascii.discount.model.dto.AsciiProductDTO;

/**
 * Converter class used to transform different layer objects. Because we want to keep the layers as separated as possible, some objects do not
 * need to pass the layers boundaries, so they have to be converted to other layers corresponding objects
 * <p/>
 * Created by cotuna on 7/4/16.
 */

public class ConverterUtil {

    /**
     * Fetches list of {@link AsciiProductDTO} objects from {@link NDJsonResponse}. {@link NDJsonResponse} contains multiple {@link AsciiProductDTO}
     * in a certain format.
     * If the corresponding {@link NDJsonResponse} doesn't have any elements, an empty list is returned
     */
    public static List<AsciiProductDTO> convertToAsciiProductDTO(NDJsonResponse response) {
        List<AsciiProductDTO> results = new ArrayList<>();

        if (response == null || response.getSplitJSONContent().length == 0) {
            return results;
        }

        Gson gsonParser = new Gson();
        for (String jsonString : response.getSplitJSONContent()) {
            AsciiProductDTO productDTO = gsonParser.fromJson(jsonString, AsciiProductDTO.class);
            results.add(productDTO);
        }

        return results;
    }

}
