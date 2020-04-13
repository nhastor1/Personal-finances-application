package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Adapter.TransactionListViewAdapter;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;

public interface MainMVP {
    public interface Interactor{
        public List<Transaction> getTransactions();
        public Account getAccount();
    }

    public interface Presenter{
        Account getAccount();
        void refreshTransactions();
        String getMonthFromName(int i);
        String getMonthName(Date date);
        int getYear(Date date);
        TransactionListViewAdapter filterAll();
        TransactionListViewAdapter filterIndividualincome();
        TransactionListViewAdapter filterRegularpayment();
        TransactionListViewAdapter filterRegularincome();
        TransactionListViewAdapter filterIndividualpayment();
        TransactionListViewAdapter filterPurchase();
        TransactionListViewAdapter filter(TransactionType type);
        TransactionListViewAdapter sortTitleASC();
        TransactionListViewAdapter sortTitleDSC();
        TransactionListViewAdapter sortPriceASC();
        TransactionListViewAdapter sortPriceDSC();
        TransactionListViewAdapter sortDateASC();
        TransactionListViewAdapter sortDateDSC();
        TransactionListViewAdapter filterDate(String mm, int year);
        double getGloabalAmount();
    }

    public interface  View{
        public TransactionPresenter getPresenter();
        public void setTransactions(List<Transaction> transactions);
        public void notifyTransactionListDataSetChanged();
    }

    public interface ActivityFuncions{
        void refreshList();
        boolean isTwoPaneMode();
    }
}
