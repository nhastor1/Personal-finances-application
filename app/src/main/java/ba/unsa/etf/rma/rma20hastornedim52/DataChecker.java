package ba.unsa.etf.rma.rma20hastornedim52;

import android.text.format.DateFormat;

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
}
