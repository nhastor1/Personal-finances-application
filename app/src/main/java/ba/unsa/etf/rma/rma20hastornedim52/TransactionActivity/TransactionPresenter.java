package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import android.content.Context;
import android.text.format.DateFormat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.R;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.Adapter.TransactionListViewAdapter;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;

public class TransactionPresenter implements MainMVP.Presenter{
    private Context context;
    private MainMVP.View view;
    private MainMVP.Interactor interactor;
    private Account account;
    private List<Transaction> transactions;

    private String[] months = {
            "January","February","March","April","May","June","July",
            "August","September","October","November","December"
    };

    public TransactionPresenter(MainMVP.View view, Context context) {
        this.context = context;
        this.view = view;
        interactor = new TransactionInteractor();

        account = interactor.getAccount();
        transactions = interactor.getTransactions();
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void refreshTransactions(){
        view.setTransactions(interactor.getTransactions());
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public String getMonthName(int i){
        if(i>=12 || i<0)
            throw new IllegalArgumentException("Wrong number of month");
        return months[i];
    }


    @Override
    public String getMonthName(Date date){
        CharSequence s  = DateFormat.format("MM", date.getTime());
        int monthNumber = Integer.parseInt((String) s)-1;
        return months[monthNumber];
    }

    @Override
    public int getYear(Date date){
        CharSequence s  = DateFormat.format("yyyy", date.getTime());
        return Integer.parseInt((String) s);
    }

    @Override
    public TransactionListViewAdapter filterAll(){
        transactions = interactor.getTransactions();
        return createAdapter();
    }

    @Override
    public TransactionListViewAdapter filterIndividualincome(){
        return filter(TransactionType.INDIVIDUALINCOME);
    }

    @Override
    public TransactionListViewAdapter filterRegularpayment(){
        return filter(TransactionType.REGULARPAYMENT);
    }

    @Override
    public TransactionListViewAdapter filterRegularincome(){
        return filter(TransactionType.REGULARINCOME);
    }

    @Override
    public TransactionListViewAdapter filterIndividualpayment(){
        return filter(TransactionType.INDIVIDUALPAYMENT);
    }

    @Override
    public TransactionListViewAdapter filterPurchase(){
        return filter(TransactionType.PURCHASE);
    }

    @Override
    public TransactionListViewAdapter filter(TransactionType type){
        transactions = new ArrayList<>();
        for(Transaction t : interactor.getTransactions())
            if(t.getType().equals(type))
                transactions.add(t);

        return createAdapter();
    }

    @Override
    public TransactionListViewAdapter sortTitleASC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t1.getTitle().compareTo(t2.getTitle());
            }
        });
        return createAdapter();
    }

    @Override
    public TransactionListViewAdapter sortTitleDSC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t2.getTitle().compareTo(t1.getTitle());
            }
        });
        return createAdapter();
    }

    @Override
    public TransactionListViewAdapter sortPriceASC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t1.getAmount() < t2.getAmount() ? 1 : -1;
            }
        });
        return createAdapter();
    }

    @Override
    public TransactionListViewAdapter sortPriceDSC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t1.getAmount() < t2.getAmount() ? -1 : 1;
            }
        });
        return createAdapter();
    }

    @Override
    public TransactionListViewAdapter sortDateASC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t1.getDate().compareTo(t2.getDate());
            }
        });
        return createAdapter();
    }

    @Override
    public TransactionListViewAdapter sortDateDSC(){
        Collections.sort(transactions, new Comparator<Transaction>(){
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
        return createAdapter();
    }

    @Override
    public TransactionListViewAdapter filterDate(String mm, int year) {
        int month = getMonthFromName(mm)-1;

        List<Transaction> newTrans = new ArrayList<>();

        for(int i=0; i<transactions.size(); i++){
            int m = getMonthNumber(transactions.get(i).getDate());
            int y = getYear(transactions.get(i).getDate());

            if(transactions.get(i).getType().equals(TransactionType.REGULARINCOME) || transactions.get(i).getType().equals(TransactionType.REGULARPAYMENT)){
                int mEnd = getMonthNumber(transactions.get(i).getEndDate());
                int yEnd = getYear(transactions.get(i).getEndDate());
                if(!(year > yEnd || year < y || (year==y && month < m) || (year==yEnd && month > mEnd)) )
                    newTrans.add(transactions.get(i));
            }
            else{
                if(!(m!=month || y!=year))
                    newTrans.add(transactions.get(i));
            }
        }

        transactions = newTrans;

        return createAdapter();
    }

    @Override
    public double getGloabalAmount(){
        double amount = account.getBudget();
        List<Transaction> transactions = interactor.getTransactions();
        for(Transaction t : transactions){
            if(t.getType().equals(TransactionType.REGULARPAYMENT) || t.getType().equals(TransactionType.REGULARINCOME)){
                amount += t.getAmount() * t.getTransactionInterval();
            }
            else{
                amount += t.getAmount();
            }
        }

        return amount;
    }

    @Override
    public int getMonthFromName(String month){
        for(int i=0; i<=11; i++)
            if(months[i].equals(month))
                return i+1;

        return -1;
    }

    private TransactionListViewAdapter createAdapter() {
        return new TransactionListViewAdapter(context.getApplicationContext(),
                R.layout.list_element_transaction, transactions);
    }

    private int getMonthNumber(Date date){
        CharSequence s  = DateFormat.format("MM", date.getTime());
        return Integer.parseInt((String) s)-1;
    }
}
