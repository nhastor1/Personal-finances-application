package ba.unsa.etf.rma.rma20hastornedim52;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class DataChecker {
    public static void validDate(String date){
        date = date.trim();
        String[] d = date.split("\\.");
        int day = Integer.parseInt(d[0]);
        int month = Integer.parseInt(d[1]);
        int year = Integer.parseInt(d[2]);

        if(year < 1 || day < 1 || month < 1 || month > 12 || day > numbOfDays(month, year))
            throw new IllegalArgumentException("Incorrect date");
    }

    public static int numbOfDays(int month, int year) {
        if(month == 4 || month == 6 || month == 9 || month  == 11)
            return 30;
        if(month != 2)
            return 31;
        if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
            return 29;
        return 28;
    }

    public static int getYear(Date date){
        CharSequence s  = DateFormat.format("yyyy", date.getTime());
        return Integer.parseInt((String) s);
    }

    public static int getMonth(Date date){
        CharSequence s  = DateFormat.format("MM", date.getTime());
        return Integer.parseInt((String) s);
    }

    public static int getDay(Date date) {
        CharSequence s  = DateFormat.format("dd", date.getTime());
        return Integer.parseInt((String) s);
    }

    public static int getCurrentDay(){
        CharSequence s  = DateFormat.format("dd", (new Date()).getTime());
        return Integer.parseInt((String) s);
    }

    public static int getCurrentMonth(){
        CharSequence s  = DateFormat.format("MM", (new Date()).getTime());
        return Integer.parseInt((String) s);
    }

    public static int getCurrentYear(){
        CharSequence s  = DateFormat.format("yyyy", (new Date()).getTime());
        return Integer.parseInt((String) s);
    }

    public static boolean isInMonth(Date d, int month){
        return getMonth(d)==month;
    }

    public static boolean isInYear(Date d, int year){
        return getYear(d)==year;
    }

    public static boolean isInMonthAndYear(Date d, int month, int year) {
        return  isInMonth(d, month) && isInYear(d, year);
    }

    public static boolean isInMonthAndYear(Calendar cal, int month, int year) {
        Date d = cal.getTime();
        return  isInMonth(d, month) && isInYear(d, year);
    }

    public static boolean isBefore(Date time, Date endDate) {
        return time.getTime() < endDate.getTime();
    }

    public static Calendar getFirstDayOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1);
        return cal;
    }

    public static Calendar getLastDayOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, numbOfDays(month, year));
        return cal;
    }
}
