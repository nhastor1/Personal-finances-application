package ba.unsa.etf.rma.rma20hastornedim52;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataChecker {
    public static void validDate(String date){
        if(date.equals(""))
            throw new IllegalArgumentException("Incorrect date");

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

    public static Date getDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        return calendar.getTime();
    }

    public static Date getDateFromService(String date){
        //date = date.substring(0, 9);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String getStringDateForService(Date date){
        if(date==null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return sdf.format(date);
    }

    public static int getIntervalsBetween(Date d1, Date d2, int interval){
        Calendar cal = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        cal.setTime(d1);
        calEnd.setTime(d2);

        int i=0;
        while(isBefore(cal.getTime(), calEnd.getTime())){
            i++;
            cal.add(Calendar.DAY_OF_MONTH, interval);
        }
        return i;
    }

    public static Date getDateFromString(String d){
        String[] date = d.split("\\.", 3);
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1, Integer.parseInt(date[0]));
        return cal.getTime();
    }

    public static int putManyTimesInThisMonth(Transaction transaction, int month, int year, List<Transaction> transactions) {
        if(!TransactionType.isRegular(transaction.getType()))
            return -1;

        Calendar cal = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        cal.setTime(transaction.getDate());
        calEnd.setTime(transaction.getEndDate());
        int times = 0;

        double total = transaction.getTotalAmount();

        while(isBefore(cal.getTime(), calEnd.getTime())){
            if(isInMonthAndYear(cal.getTime(), month, year)) {
                try {
                    Transaction t = (Transaction) transaction.clone();
                    t.setDate(cal.getTime());
                    t.setOrginalAmount(total);
                    transactions.add(t);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                times++;
            }

            cal.add(Calendar.DAY_OF_MONTH, transaction.getTransactionInterval());
        }

        return times;
    }
}
