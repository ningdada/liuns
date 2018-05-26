package util;

public class StringUtil {

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0 ? true : false;
    }

    public static boolean isNotEmpty(String value) {
        return value == null || value.trim().length() == 0 ? false : true;
    }


}
