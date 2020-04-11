package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity.MainMVP;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;

public class TransactionInteractor implements MainMVP.Interactor {
    @Override
    public List<Transaction> getTransactions(){
        return TransactionModel.transactions;
    }

    @Override
    public Account getAccount(){
        return TransactionModel.account;
    }
}
