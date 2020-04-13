package ba.unsa.etf.rma.rma20hastornedim52.Graphs;

import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;

public class GraphsInteractor implements GraphsMVP.Interactor {

    @Override
    public List<Transaction> getTransactions(){
        return TransactionModel.transactions;
    }
}
