package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;

public interface MainMVP {
    public interface Interactor{
        public List<Transaction> getTransactions();
        public Account getAccount();
    }

    public interface Presenter{

    }

    public interface  View{
        public TransactionPresenter getPresenter();
        public void setTransactions(List<Transaction> transactions);
        public void notifyTransactionListDataSetChanged();
    }

    public interface RefreshListFragment{
        void refreshList();
        boolean isTwoPaneMode();
    }
}
