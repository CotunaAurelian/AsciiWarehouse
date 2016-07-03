package com.xteam.warehouse.ascii.discount.business.webservices.responses;


/**
 * NDJSon response wrapper. This class holds accessible JSON content parts
 * Created by Wraith on 7/2/2016.
 */

public class NDJsonResponse extends BaseResponse {

    /**
     * JSON parts from the raw content returned in the NDJSON format
     */
    private String[] mSplitJSONContent;

    public NDJsonResponse(String rawContent) {
        //We make sure that the NDJSON format is respected and there is a new line character after
        //each part, and before the next one. We are going to split the segments of NDJSON based
        //on that caracter
        String filteredRawContent = rawContent.replaceAll("\\}\\s*\\{", "\\}\\%n\\{");

        this.mSplitJSONContent = filteredRawContent.split("%n");
        setResponseType(NDJSON_RESPONSE);
    }

    public String[] getSplitJSONContent() {
        return mSplitJSONContent;
    }
}
