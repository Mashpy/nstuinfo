package com.mashpy.nstuinfo.mOtherUtils;

/**
 * Created by whoami on 10/30/2018.
 */

public class ExtraUtils {

    public static int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }

}
