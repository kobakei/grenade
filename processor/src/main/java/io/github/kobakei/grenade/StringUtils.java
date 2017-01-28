package io.github.kobakei.grenade;

/**
 * Utility class for processor
 * Created by keisuke on 2017/01/28.
 */

class StringUtils {

    /**
     * {1,2,3} => "1,2,3"
     * @param a
     * @return
     */
    static String join(int[] a) {
        String str = "";
        for (int i = 0; i < a.length; i++) {
            str += a[i];
            if (i < a.length - 1) {
                str += ",";
            }
        }
        return str;
    }

    /**
     * "fooBar" => "FooBar"
     * @param str
     * @return
     */
    static String beginCap(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
