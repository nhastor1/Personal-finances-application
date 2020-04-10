package ba.unsa.etf.rma.rma20hastornedim52.Budget;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;

public class BudgetInteractor implements BudgetMVP.Interactor {

    @Override
    public Account getAccount(){
        return TransactionModel.account;
    }

    @Override
    public void updateAccount(Account account){
        TransactionModel.account = account;
    }
}
