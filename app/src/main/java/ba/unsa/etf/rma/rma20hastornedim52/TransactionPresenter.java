package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.Context;

import java.util.List;

public class TransactionPresenter implements MainMVP.Presenter{
    private Context context;
    private MainMVP.View view;
    private MainMVP.Interactor interactor;
    private Account account;
    private List<Transaction> transactions;

    public TransactionPresenter(MainMVP.View view, Context context) {
        this.context = context;
        this.view = view;
        interactor = new TransactionInteractor();
        account = new Account(500000, 1000000, 100000);

        transactions = interactor.get();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void refreshTransactions(){
        view.setTransactions(interactor.get());
        view.notifyMovieListDataSetChanged();
    }
}
