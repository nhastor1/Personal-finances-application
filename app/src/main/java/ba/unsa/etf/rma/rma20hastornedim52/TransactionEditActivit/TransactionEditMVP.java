package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;

import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;

public interface TransactionEditMVP {
    public interface Interactor{
        public List<Transaction> getTransactions();
        public Account getAccount();
    }

    public interface Presenter{
        public double getTotalAmount();
        public double getMonthAmount(Date date);
        public Transaction getTransaction(int id);
        public void addTransaction(Transaction transaction);
        public void removeTransaction(Transaction transaction);
        public Account getAccount();
    }

    public interface  View{

    }
}
