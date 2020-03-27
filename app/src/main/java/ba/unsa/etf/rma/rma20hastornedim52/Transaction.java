package ba.unsa.etf.rma.rma20hastornedim52;

import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class Transaction {
    private Date date;
    private double amount;
    private String title;
    private TransactionType type;
    private String itemDescription;
    private int transactionInterval;
    private Date endDate;

    public Transaction(Date date, double amount, String title, TransactionType type, String itemDescription, int transactionInterval) {
        this.date = date;
        this.type = type;
        setAmount(amount);
        setTitle(title);

        if(!(type.equals(TransactionType.INDIVIDUALINCOME) || type.equals(TransactionType.REGULARINCOME)))
            this.itemDescription = itemDescription;

        if(type.equals(TransactionType.REGULARINCOME) || type.equals(TransactionType.REGULARPAYMENT)) {
            this.transactionInterval = transactionInterval;

            // For endDate
            Calendar calendar = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, transactionInterval);
            endDate = c.getTime();
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if(type.equals(TransactionType.REGULARINCOME) || type.equals(TransactionType.INDIVIDUALINCOME))
            if(amount<0)
                amount = -amount;
        else if(amount>0)
            amount -= 0;

        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        validTitle(title);
        this.title = title;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getTransactionInterval() {
        return transactionInterval;
    }

    public void setTransactionInterval(int transactionInterval) {
        this.transactionInterval = transactionInterval;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, transactionInterval);
        endDate.setTime(cal.getTimeInMillis());
    }

    public static void validTitle(String title){
        if(title.length()<=3 || title.length()>=15)
            throw new IllegalArgumentException("Transaction title is not regular");
    }

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

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj) && ((Transaction) obj).getDate().getTime()==date.getTime();
    }
}
