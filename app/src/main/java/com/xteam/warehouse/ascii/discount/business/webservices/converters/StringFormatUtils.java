package com.xteam.warehouse.ascii.discount.business.webservices.converters;

import java.util.List;

/**
 * Utility class for different String formatting
 * Created by Wraith on 7/3/2016.
 */

public class StringFormatUtils {

    /**
     * Default whitespace delimiter for strings
     */
    private static final String WHITESPACE_DELIMITER = " ";

    /**
     * Append the arguments with provided delimiter. The delimiter is not added after the last element.
     * If the delimiter is null, whitespace character will be added by default.
     * If the argument is null or with length 0, an empty string is returned
     */
    public static String appendStringsWithDelimiter(List<String> parameters, String delimiter) {
        if (parameters == null || parameters.size() == 0) {
            return "";
        }
        String currentDelimiter = (delimiter == null) ? WHITESPACE_DELIMITER : delimiter;
        StringBuilder builder = new StringBuilder();
        for (int iterator = 0; iterator < parameters.size(); iterator++) {
            builder.append(parameters.get(iterator));
            //Append the whitespace hex to all, except the last position
            if (iterator + 1 < parameters.size())
                builder.append(currentDelimiter);
        }
        return builder.toString();
    }
}
