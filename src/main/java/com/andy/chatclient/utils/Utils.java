package com.andy.chatclient.utils;

public class Utils {

    /**
     * This function checks if a string is null or only contains spaces.
     * @param aString
     * @return
     */
    public static boolean isEmptyOrNullString(String aString){
        if(aString == null)
            return true;

        return aString.trim().isEmpty();
    }

    /**
     * Turns the first letter of a string to upper case and the remaining to lower case
     * @param aString
     * @return
     */
    public static String capitalize(String aString){

        StringBuilder sb = new StringBuilder();

        for(String chunk : aString.split(" "))
            sb.append(capitalizeNonSpacedString(chunk)).append(" ");

        return sb.toString().trim();
    }

    public static String capitalizeNonSpacedString(String nonSpacedString){
        if(isEmptyOrNullString(nonSpacedString))
            return "";

        return nonSpacedString.substring(0, 1).toUpperCase() + nonSpacedString.substring(1).toLowerCase();
    }
}
