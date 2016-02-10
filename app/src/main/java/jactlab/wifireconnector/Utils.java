package jactlab.wifireconnector;

import android.util.Log;

/**
 * Created by KIMMANSUNG-NOTE on 2016-02-04.
 */
public class Utils {

    private static String psTag = "mstag";

    static void logd(String in) {
        if(StaticData.DGB_LEVEL == StaticData.DGB_LEVEL_1) {
            Log.d(psTag, in);
        }
    }
}
