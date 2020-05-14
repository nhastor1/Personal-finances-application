package ba.unsa.etf.rma.rma20hastornedim52;

import android.icu.text.SimpleDateFormat;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class Transaction implements Parcelable, Parcelable.Creator<Transaction> {
    private int id;
    private Date date;
    private double amount;
    private String title;
    private TransactionType type;
    private String itemDescription;
    private int transactionInterval;
    private Date endDate;

    public Transaction(){};

    public Transaction(Date date, double amount, String title, TransactionType type, String itemDescription) {
        this.date = date;
        this.type = type;
        setAmount(amount);
        setTitle(title);

        if(!(type.equals(TransactionType.INDIVIDUALINCOME) || type.equals(TransactionType.REGULARINCOME)))
            this.itemDescription = itemDescription;

        if(type.equals(TransactionType.REGULARINCOME) || type.equals(TransactionType.REGULARPAYMENT)) {
            this.transactionInterval = 0;
            endDate = null;
        }
    }

    public Transaction(Date date, double amount, String title, TransactionType type, String itemDescription, int transactionInterval, Date endDate) {
        this.date = date;
        this.type = type;
        setAmount(amount);
        setTitle(title);

        if(!(type.equals(TransactionType.INDIVIDUALINCOME) || type.equals(TransactionType.REGULARINCOME)))
            this.itemDescription = itemDescription;

        if(type.equals(TransactionType.REGULARINCOME) || type.equals(TransactionType.REGULARPAYMENT)) {
            this.transactionInterval = transactionInterval;
            this.endDate = endDate;
        }
    }

    protected Transaction(Parcel in) {
        id = in.readInt();
        amount = in.readDouble();
        title = in.readString();
        itemDescription = in.readString();
        transactionInterval = in.readInt();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isInMonth(int month, int year){
        int y = DataChecker.getYear(getDate());
        int m = DataChecker.getMonth(getDate());

        return y == year && m == month;
    }

    public boolean isInYear(int year) {
        return DataChecker.getYear(getDate()) == year;
    }

    public boolean isBetween(int month, int year){
        int y1 = DataChecker.getYear(getDate());
        int m1 = DataChecker.getMonth(getDate());
        int m2 = DataChecker.getMonth(getEndDate());
        int y2 = DataChecker.getYear(getEndDate());

        if((year > y1 && year < y2) ||
                ((year == y1 || year == y2) && month >= m1 && month <= m2))
            return true;

        return false;
    }

    public static void validTitle(String title){
        if(title.length()<=3 || title.length()>15)
            throw new IllegalArgumentException("Transaction title is not regular");
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj) && ((Transaction) obj).getDate().getTime()==date.getTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(amount);
        dest.writeString(title);
        dest.writeString(itemDescription);
        dest.writeInt(transactionInterval);
    }

    @Override
    public Transaction createFromParcel(Parcel source) {
        return null;
    }

    @Override
    public Transaction[] newArray(int size) {
        return new Transaction[0];
    }

}
