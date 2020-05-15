package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;

import android.content.Context;
import android.view.SurfaceControl;

import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity.TransactionInteractor;

public class TransactionEditPresenter implements TransactionEditMVP.Presenter,
    TransactionEditInteractor.OnTransactionAddDone, TransactionEditInteractor.OnTransactionRemoveDone,
    TransactionEditInteractor.OnTransactionEditDone{

    private Context context;
    private TransactionEditMVP.View view;
    private TransactionEditMVP.Interactor interactor;
    private Transaction transaction;

    public TransactionEditPresenter(TransactionEditMVP.View view, Context context, Transaction transaction) {
        this.context = context;
        this.view = view;
        this.transaction = transaction;
        interactor = new TransactionEditInteractor( (TransactionEditMVP.Presenter) this, transaction);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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
        (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionAddDone) this, transaction)).execute();
    }

    @Override
    public void removeTransaction(Transaction transaction) {
        interactor.removeTransaction(transaction);
    }

    @Override
    public Account getAccount() {
        return interactor.getAccount();
    }

    @Override
    public void onAddDone(Transaction transaction) {
        // Add transaction in list
    }

    @Override
    public void onEditDone(Transaction transaction) {
        // Edit transaction in list
    }

    @Override
    public void onRemoveDone(Transaction transaction) {
        // Remove transaction from list
    }
}
