package ba.unsa.etf.rma.rma20hastornedim52;

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
        this.amount = amount;
        setTitle(title);
        this.type = type;

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
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title.length()<=3 || title.length()>=15)
            throw new IllegalArgumentException("Transaction title is not regular");
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

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj) && ((Transaction) obj).getDate().getTime()==date.getTime();
    }
}
