
package com.uwetrottmann.shopr.utils;

public class Utils {

    /**
     * Returns the first http url out of the given array in the form of
     * "http://abc | http://cde".
     */
    public static String extractFirstUrl(String arrayAsString) {
        String url = arrayAsString.split(" | ")[0];
        return url;
    }
    
    public static String[] extractUrls(String arrayAsString) {
        return arrayAsString.split(" \\| ");
    }

}
