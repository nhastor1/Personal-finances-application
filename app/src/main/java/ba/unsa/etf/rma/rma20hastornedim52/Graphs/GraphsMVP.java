package ba.unsa.etf.rma.rma20hastornedim52.Graphs;


import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;

public interface GraphsMVP {
    public interface View{
        GraphsMVP.Presenter getPresenter();
    }

    public interface Presenter{
        double[] getDayPayments();

        double[] getDayIncome();

        double[] getDayAll();

        double[] getMonthsPayments();

        double[] getMonthsIncome();

        double[] getMonthsAll();
    }

    public interface Interactor{
        List<Transaction> getTransactions();
    }
}
