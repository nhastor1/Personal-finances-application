package ba.unsa.etf.rma.rma20hastornedim52.Graphs;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;

public class GraphsPresenter implements GraphsMVP.Presenter {

    private Context context;
    private GraphsMVP.Interactor interactor;
    private GraphsMVP.View view;
    private List<Transaction> transactions;

    public GraphsPresenter(GraphsMVP.View view, Context context) {
        this.context = context;
        this.interactor = new GraphsInteractor();
        this.view = view;
        transactions = interactor.getTransactions();
    }

    @Override
    public double[] getDayPayments(){
        int month = DataChecker.getCurrentMonth();
        int year = DataChecker.getCurrentYear();
        int n = DataChecker.numbOfDays(month, year);
        double[] days = new double[n];


        for(Transaction t : transactions)
            if( t.isInMonth(month, year) && (t.getType().equals(TransactionType.INDIVIDUALPAYMENT) || t.getType().equals(TransactionType.PURCHASE)))
                days[DataChecker.getDay(t.getDate())] += t.getAmount();
            else if(t.getType().equals(TransactionType.REGULARPAYMENT) && t.isBetween(month, year)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getDate());
                while(DataChecker.isBefore(cal.getTime(), t.getEndDate())){
                    if(DataChecker.isInMonthAndYear(cal.getTime(), month, year)){
                        days[DataChecker.getDay(cal.getTime())] += t.getAmount();
                    }
                    cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                }
            }

        return days;
    }

    @Override
    public double[] getDayIncome() {
        int month = DataChecker.getCurrentMonth();
        int year = DataChecker.getCurrentYear();
        int n = DataChecker.numbOfDays(month, year);
        double[] days = new double[n];


        for(Transaction t : transactions)
            if( t.isInMonth(month, year) && t.getType().equals(TransactionType.INDIVIDUALINCOME))
                days[DataChecker.getDay(t.getDate())] += t.getAmount();
            else if(t.getType().equals(TransactionType.REGULARINCOME) && t.isBetween(month, year)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getDate());
                while(DataChecker.isBefore(cal.getTime(), t.getEndDate())){
                    if(DataChecker.isInMonthAndYear(cal.getTime(), month, year)){
                        days[DataChecker.getDay(cal.getTime())] += t.getAmount();
                    }
                    cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                }
            }

        return days;
    }

    @Override
    public double[] getDayAll(){
        double[] income = getDayIncome();
        double[] payments = getDayPayments();
        double[] all = new double[income.length];
        all[0] = income[0] - payments[0];

        for(int i=1; i<income.length; i++)
            all[i] = all[i-1] + income[i] + payments[i];

        return all;
    }

    @Override
    public double[] getMonthsPayments(){
        int month = DataChecker.getCurrentMonth();
        int year = DataChecker.getCurrentYear();
        double[] days = new double[12];


        for(Transaction t : transactions)
            if( t.isInYear(year) && (t.getType().equals(TransactionType.INDIVIDUALPAYMENT) || t.getType().equals(TransactionType.PURCHASE)))
                days[DataChecker.getMonth(t.getDate())-1] += t.getAmount();
            else if(t.getType().equals(TransactionType.REGULARPAYMENT) && t.isBetween(month, year)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getDate());
                while(DataChecker.isBefore(cal.getTime(), t.getEndDate())){
                    if(DataChecker.isInYear(cal.getTime(), year)){
                        days[DataChecker.getMonth(cal.getTime())-1] += t.getAmount();
                    }
                    cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                }
            }

        return days;
    }

    @Override
    public double[] getMonthsIncome() {
        int month = DataChecker.getCurrentMonth();
        int year = DataChecker.getCurrentYear();
        double[] days = new double[12];


        for(Transaction t : transactions)
            if( t.isInYear(year) && t.getType().equals(TransactionType.INDIVIDUALINCOME))
                days[DataChecker.getMonth(t.getDate())-1] += t.getAmount();
            else if(t.getType().equals(TransactionType.REGULARINCOME) && t.isBetween(month, year)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getDate());
                while(DataChecker.isBefore(cal.getTime(), t.getEndDate())){
                    if(DataChecker.isInYear(cal.getTime(), year)){
                        days[DataChecker.getMonth(cal.getTime())-1] += t.getAmount();
                    }
                    cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                }
            }

        return days;
    }

    @Override
    public double[] getMonthsAll() {
        double[] income = getMonthsIncome();
        double[] payments = getMonthsPayments();
        double[] all = new double[income.length];
        all[0] = income[0] - payments[0];

        for(int i=1; i<income.length; i++)
            all[i] = all[i-1] + income[i] + payments[i];

        return all;
    }


}
