package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.SurfaceControl;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        transactions = interactor.getTransactions();
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
        view.setTransactions(interactor.getTransactions());
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

    public TransactionListViewAdapter filterAll(){
        transactions = interactor.getTransactions();
        return createAdapter();
    }

    public TransactionListViewAdapter filterIndividualincome(){
        return filter(TransactionType.INDIVIDUALINCOME);
    }

    public TransactionListViewAdapter filterRegularpayment(){
        return filter(TransactionType.REGULARPAYMENT);
    }

    public TransactionListViewAdapter filterRegularincome(){
        return filter(TransactionType.REGULARINCOME);
    }

    public TransactionListViewAdapter filterIndividualpayment(){
        return filter(TransactionType.INDIVIDUALPAYMENT);
    }

    public TransactionListViewAdapter filterPurchase(){
        return filter(TransactionType.PURCHASE);
    }

    public TransactionListViewAdapter filter(TransactionType type){
        transactions = new ArrayList<>();
        for(Transaction t : interactor.getTransactions())
            if(t.getType().equals(type))
                transactions.add(t);

        return createAdapter();
    }

    public TransactionListViewAdapter sortTitleASC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t1.getTitle().compareTo(t2.getTitle());
            }
        });
        return createAdapter();
    }

    public TransactionListViewAdapter sortTitleDSC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t2.getTitle().compareTo(t1.getTitle());
            }
        });
        return createAdapter();
    }

    public TransactionListViewAdapter sortPriceASC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t1.getAmount() < t2.getAmount() ? 1 : -1;
            }
        });
        return createAdapter();
    }

    public TransactionListViewAdapter sortPriceDSC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t1.getAmount() < t2.getAmount() ? -1 : 1;
            }
        });
        return createAdapter();
    }

    public TransactionListViewAdapter sortDateASC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t1.getDate().compareTo(t2.getDate());
            }
        });
        return createAdapter();
    }

    public TransactionListViewAdapter sortDateDSC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
        return createAdapter();
    }

    private TransactionListViewAdapter createAdapter() {
        return new TransactionListViewAdapter(context.getApplicationContext(),
                R.layout.list_element_transaction, transactions);
    }
}
