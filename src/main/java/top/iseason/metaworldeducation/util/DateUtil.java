package top.iseason.metaworldeducation.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将yyyy-mm-dd HH:mm:ss格式的日期转换为Date格式
     */
    public static Date toDate(String dateString) throws Exception {
        return format.parse(dateString);
    }

    /**
     * 键时间输出为 yyyy-mm-dd HH:mm:ss 的形式
     */
    public static String toString(Date date) {
        return format.format(date);
    }

}