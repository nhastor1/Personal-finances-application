package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;

import android.view.SurfaceControl;

import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;

public class TransactionEditInteractor implements TransactionEditMVP.Interactor {
    @Override
    public List<Transaction> getTransactions() {
        return TransactionModel.transactions;
    }

    @Override
    public Account getAccount() {
        return TransactionModel.account;
    }

    @Override
    public void removeTransaction(Transaction transaction){
        TransactionModel.transactions.remove(transaction);
    }
}
