package datetime;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTime {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss aa");

    public static String getCurrentDate() { return simpleDateFormat.format(new Date(System.currentTimeMillis())); }

    public static String getCurrentTime() { return simpleTimeFormat.format(new Time(System.currentTimeMillis())); }

    public static Date parseDate(String dateString) {

        try {

            java.util.Date date = simpleDateFormat.parse(dateString);

            return new Date(date.getTime());
        }
        catch (ParseException e) { throw new RuntimeException(e); }
    }

    public static Time parseTime(String timeString) {

        try {

            long ms = simpleTimeFormat.parse(timeString).getTime();

            return new Time(ms);
        }
        catch (ParseException e) { throw new RuntimeException(e); }
    }

    public static String dateToString(Date date) { return simpleDateFormat.format(date); }

    public static String timeToString(Time time) { return  simpleTimeFormat.format(time); }
}