package site.lool.android.competition.utils;

public class StringUtils {

    public static String getNameOfPath(String str) {
        return str.substring(str.lastIndexOf("/")+1);
    }

}
