package micronet.com.rebootservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by eemaan.siddiqi on 12/27/2016.
 */
public class Utils {

        public static String formatDate(Date date) {
            return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);
        }

        public static String formatDateShort(Date date) {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        /**
         *Required format for MCU Date: year-month-day hour:min:sec.deciseconds
         *Example: Ex : 2016-03-29 19:09:06.58
         */
        public static String formatDateForRTC(Date date, boolean toUtc){
            if(!toUtc){
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
            return simpleDateFormat.format(date);
         }

         public static String formatUptime(Date date){
            return new SimpleDateFormat("HH:mm:ss").format(date);
         }

        public static String formatDateShort(long time) {
            return formatDateShort(new Date(time));
        }

        public static String formatDate(long time) {
            return formatDate(new Date(time));
        }

        public static String formatDateForRTC(long time, boolean toUtc) {
            return formatDateForRTC(new Date(time),toUtc);
        }

        public static String formatUptime(long time) {
            return formatUptime(new Date(time));
        }
    }
