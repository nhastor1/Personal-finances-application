package ba.unsa.etf.rma.rma20hastornedim52;

import java.util.List;

public class TransactionInteractor implements MainMVP.Interactor {
    public List<Transaction> getTransactions(){
        return TransactionModel.transactions;
    }
}
