package ba.unsa.etf.rma.rma20hastornedim52;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionModel {
    public static Account account = new Account(1, 0, 0, 0);

    public static List<Transaction> transactions = new ArrayList<Transaction>(){
        {
//            Calendar calendar = Calendar.getInstance();
//            Calendar endcal = Calendar.getInstance();
//
//            calendar.set(2020, 2, 23);
//            add(new Transaction(calendar.getTime(), 500, "Transaction 1", TransactionType.INDIVIDUALINCOME, null));
//
//            calendar.set(2020, 1, 13);
//            add(new Transaction(calendar.getTime(), 800, "Transaction 2", TransactionType.INDIVIDUALINCOME, null));
//
//            calendar.set(2020, 0, 29);
//            add(new Transaction(calendar.getTime(), 1500, "Transaction 3", TransactionType.INDIVIDUALINCOME, null));
//
//            calendar.set(2020, 2, 5);
//            add(new Transaction(calendar.getTime(), 2500, "Transaction 4", TransactionType.INDIVIDUALINCOME, null));
//
//            calendar.set(2020, 3, 2);
//            add(new Transaction(calendar.getTime(), -100, "Transaction 5", TransactionType.INDIVIDUALPAYMENT, "TV"));
//
//            calendar.set(2020, 2, 16);
//            add(new Transaction(calendar.getTime(), -500, "Transaction 6", TransactionType.INDIVIDUALPAYMENT, "Mobitel"));
//
//            calendar.set(2020, 1, 23);
//            add(new Transaction(calendar.getTime(), -1200, "Transaction 7", TransactionType.INDIVIDUALPAYMENT, "Laptop"));
//
//            calendar.set(2020, 0, 21);
//            add(new Transaction(calendar.getTime(), -20, "Transaction 8", TransactionType.INDIVIDUALPAYMENT, "Knjiga"));
//
//            calendar.set(2020, 3, 5);
//            add(new Transaction(calendar.getTime(), -1000, "Transaction 9", TransactionType.PURCHASE, "NK Azot"));
//
//            calendar.set(2020, 1, 22);
//            add(new Transaction(calendar.getTime(), -5000, "Transaction 10", TransactionType.PURCHASE, "BH Telecom"));
//
//            calendar.set(2020, 2, 11);
//            add(new Transaction(calendar.getTime(), -4000, "Transaction 11", TransactionType.PURCHASE, "Prevent"));
//
//            calendar.set(2020, 0, 30);
//            add(new Transaction(calendar.getTime(), -2000, "Transaction 12", TransactionType.PURCHASE, "Pobjeda"));
//
//            calendar.set(2020, 3, 17);
//            endcal.set(2020, 9, 15);
//            add(new Transaction(calendar.getTime(), 100, "Transaction 13", TransactionType.REGULARINCOME, null, 12, endcal.getTime()));
//
//            calendar.set(2020, 0, 16);
//            endcal.set(2020, 10, 15);
//            add(new Transaction(calendar.getTime(), 200, "Transaction 14", TransactionType.REGULARINCOME, null, 24, endcal.getTime()));
//
//            calendar.set(2020, 2, 13);
//            endcal.set(2021, 3, 13);
//            add(new Transaction(calendar.getTime(), 150, "Transaction 15", TransactionType.REGULARINCOME, null, 48, endcal.getTime()));
//
//            calendar.set(2020, 1, 29);
//            endcal.set(2022, 1, 12);
//            add(new Transaction(calendar.getTime(), 50, "Transaction 16", TransactionType.REGULARINCOME, null, 6, endcal.getTime()));
//
//            calendar.set(2020, 1, 1);
//            endcal.set(2021, 4, 1);
//            add(new Transaction(calendar.getTime(), -300, "Transaction 17", TransactionType.REGULARPAYMENT, "Kredit", 48, endcal.getTime()));
//
//            calendar.set(2020, 2, 5);
//            endcal.set(2020, 6, 23);
//            add(new Transaction(calendar.getTime(), -100, "Transaction 18", TransactionType.REGULARPAYMENT, "Automobil", 30, endcal.getTime()));
//
//            calendar.set(2020, 3, 28);
//            endcal.set(2021, 3, 13);
//            add(new Transaction(calendar.getTime(), -10, "Transaction 19", TransactionType.REGULARPAYMENT, "Režije", 90, endcal.getTime()));
//
//            calendar.set(2020, 1, 14);
//            endcal.set(2026, 2, 5);
//            add(new Transaction(calendar.getTime(), -20, "Transaction 20", TransactionType.REGULARPAYMENT, "Kirija", 60, endcal.getTime()));

        }
    };
}
