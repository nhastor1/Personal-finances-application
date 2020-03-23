package ba.unsa.etf.rma.rma20hastornedim52;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionModel implements MainMVP.Model {
    public static List<Transaction> transactions = new ArrayList<Transaction>(){
        {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2020, 3, 23);
            add(new Transaction(calendar.getTime(), 500, "Transaction 1", TransactionType.INDIVIDUALINCOME, null, 0));

            calendar.set(2020, 2, 13);
            add(new Transaction(calendar.getTime(), 800, "Transaction 2", TransactionType.INDIVIDUALINCOME, null, 0));

            calendar.set(2020, 1, 29);
            add(new Transaction(calendar.getTime(), 1500, "Transaction 3", TransactionType.INDIVIDUALINCOME, null, 0));

            calendar.set(2020, 3, 5);
            add(new Transaction(calendar.getTime(), 2500, "Transaction 4", TransactionType.INDIVIDUALINCOME, null, 0));

            calendar.set(2020, 4, 2);
            add(new Transaction(calendar.getTime(), 100, "Transaction 5", TransactionType.INDIVIDUALPAYMENT, "TV", 0));

            calendar.set(2020, 3, 16);
            add(new Transaction(calendar.getTime(), 500, "Transaction 6", TransactionType.INDIVIDUALPAYMENT, "Mobitel", 0));

            calendar.set(2020, 2, 23);
            add(new Transaction(calendar.getTime(), 1200, "Transaction 7", TransactionType.INDIVIDUALPAYMENT, "Laptop", 0));

            calendar.set(2020, 1, 21);
            add(new Transaction(calendar.getTime(), 20, "Transaction 8", TransactionType.INDIVIDUALPAYMENT, "Knjiga", 0));

            calendar.set(2020, 4, 5);
            add(new Transaction(calendar.getTime(), 10000, "Transaction 9", TransactionType.PURCHASE, "NK Azot", 0));

            calendar.set(2020, 2, 22);
            add(new Transaction(calendar.getTime(), 50000, "Transaction 10", TransactionType.PURCHASE, "BH Telecom", 0));

            calendar.set(2020, 3, 11);
            add(new Transaction(calendar.getTime(), 40000, "Transaction 11", TransactionType.PURCHASE, "Prevent", 0));

            calendar.set(2020, 1, 30);
            add(new Transaction(calendar.getTime(), 20000, "Transaction 12", TransactionType.PURCHASE, "Pobjeda", 0));

            calendar.set(2020, 4, 17);
            add(new Transaction(calendar.getTime(), 100, "Transaction 13", TransactionType.REGULARINCOME, null, 12));

            calendar.set(2020, 1, 16);
            add(new Transaction(calendar.getTime(), 200, "Transaction 14", TransactionType.REGULARINCOME, null, 24));

            calendar.set(2020, 3, 13);
            add(new Transaction(calendar.getTime(), 150, "Transaction 15", TransactionType.REGULARINCOME, null, 48));

            calendar.set(2020, 2, 29);
            add(new Transaction(calendar.getTime(), 50, "Transaction 16", TransactionType.REGULARINCOME, null, 6));

            calendar.set(2020, 2, 1);
            add(new Transaction(calendar.getTime(), 300, "Transaction 17", TransactionType.REGULARPAYMENT, "Kredit", 48));

            calendar.set(2020, 3, 5);
            add(new Transaction(calendar.getTime(), 100, "Transaction 18", TransactionType.REGULARPAYMENT, "Automobil", 200));

            calendar.set(2020, 4, 28);
            add(new Transaction(calendar.getTime(), 10, "Transaction 19", TransactionType.REGULARPAYMENT, "Režije", 10000));

            calendar.set(2020, 2, 14);
            add(new Transaction(calendar.getTime(), 20, "Transaction 20", TransactionType.REGULARPAYMENT, "Kirija", 500));

        }
    };
}
