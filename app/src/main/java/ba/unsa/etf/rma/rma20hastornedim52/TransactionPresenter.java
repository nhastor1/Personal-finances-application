package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;
import java.util.List;

public class TransactionPresenter implements MainMVP.Presenter{
    private Context context;
    private MainMVP.View view;
    private MainMVP.Interactor interactor;
    private Account account;
    private List<Transaction> transactions;

    private String[] months = {
            "", "January","February","March","April","May","June","July",
            "August","September","October","November","December"
    };

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

    public String getMonth(int i){
        if(i>12 || i<1)
            throw new IllegalArgumentException("Wrong number of month");
        return months[i];
    }

    public String getMonth(Date date){
        CharSequence s  = DateFormat.format("MM", date.getTime());
        int monthNumber = Integer.parseInt((String) s);
        return months[monthNumber];
    }

    public int getYear(Date date){
        CharSequence s  = DateFormat.format("yyyy", date.getTime());
        return Integer.parseInt((String) s);
    }
}
