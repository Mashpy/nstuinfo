package com.nstuinfo.mJsonUtils;

/**
 * Created by whoami on 11/2/2018.
 */

public class Constants {

    //public static String JSON_CHECKING_URL = "https://nstudiary.nstu.edu.bd/checkdata.json";
    //public static String JSON_DATA_URL = "https://nstudiary.nstu.edu.bd/nstudiary.json";

    public static final String JSON_CHECKING_URL = "http://nstudiary.nstu.edu.bd/api/versionapi";
    public static final String JSON_DATA_URL = "http://nstudiary.nstu.edu.bd/api/infoapi";

    public static final String URL = "http://nstu.edu.bd/";


    public static Boolean UPDATE_AVAILABLE = false;

    public static void INITIALIZE_CONSTANTS () {
        UPDATE_AVAILABLE = false;
    }

}
