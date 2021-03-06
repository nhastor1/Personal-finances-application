package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;

import android.content.Context;

import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;

public class TransactionEditPresenter implements TransactionEditMVP.Presenter {

    private Context context;
    private TransactionEditMVP.View view;
    private TransactionEditMVP.Interactor interactor;

    public TransactionEditPresenter(TransactionEditMVP.View view, Context context) {
        this.context = context;
        this.view = view;
        interactor = new TransactionEditInteractor();
    }

    @Override
    public double getTotalAmount() {
        List<Transaction> trans = interactor.getTransactions();
        double total = 0;

        for(Transaction t : trans)
            total += t.getAmount();

        return total;
    }

    @Override
    public double getMonthAmount(Date date) {
        List<Transaction> trans = interactor.getTransactions();
        double total = 0;

        int month = DataChecker.getMonth(date);
        int year = DataChecker.getYear(date);

        for(Transaction t : trans)
            if(DataChecker.getMonth(date) == month &&
                    DataChecker.getYear(date) == year)
                total += t.getAmount();

        return total;
    }

    @Override
    public Transaction getTransaction(int id){
        return interactor.getTransactions().get(id);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        interactor.getTransactions().add(transaction);
    }

    @Override
    public void removeTransaction(Transaction transaction) {
        interactor.getTransactions().remove(transaction);
    }

    @Override
    public Account getAccount() {
        return interactor.getAccount();
    }
}
