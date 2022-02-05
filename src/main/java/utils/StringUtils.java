package utils;

public class StringUtils {

    public static String EMPTY = "";

    public static boolean isNotEmpty(String s) {
        return s != null && !s.equals("");
    }

    public static String numberize(int number) {
        String numberStr = String.valueOf(number);
        if (number > 999) {
            numberStr = numberStr.substring(0, numberStr.length() - 3) + "," + numberStr.substring(numberStr.length() - 3);
        }
        if (number > 999999) {
            numberStr = numberStr.substring(0, numberStr.length() - 7) + "," + numberStr.substring(numberStr.length() - 7);
        }
        return numberStr;
    }
}
