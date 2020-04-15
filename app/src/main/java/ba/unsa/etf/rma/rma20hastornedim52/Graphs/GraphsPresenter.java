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

    private double[] daysIncome;
    private double[] daysPayments;
    private double[] monthsIncome;
    private double[] monthsPayments;
    private double[] weeksIncome;
    private double[] weeksPayments;


    public GraphsPresenter(GraphsMVP.View view, Context context) {
        this.context = context;
        this.interactor = new GraphsInteractor();
        this.view = view;
        transactions = interactor.getTransactions();
    }

    @Override
    public double[] getDayPayments(){
        if(daysPayments!=null)
            return daysPayments;

        int month = DataChecker.getCurrentMonth();
        int year = DataChecker.getCurrentYear();
        int n = DataChecker.numbOfDays(month, year);
        double[] days = new double[n];


        for(Transaction t : transactions)
            if( t.isInMonth(month, year) && (t.getType().equals(TransactionType.INDIVIDUALPAYMENT) || t.getType().equals(TransactionType.PURCHASE)))
                days[DataChecker.getDay(t.getDate())-1] += t.getAmount();
            else if(t.getType().equals(TransactionType.REGULARPAYMENT) && t.isBetween(month, year)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getDate());
                while(DataChecker.isBefore(cal.getTime(), t.getEndDate())){
                    if(DataChecker.isInMonthAndYear(cal.getTime(), month, year)){
                        days[DataChecker.getDay(cal.getTime())-1] += t.getAmount();
                    }
                    cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                }
            }

        daysPayments = days;
        return days;
    }

    @Override
    public double[] getDayIncome() {
        if(daysIncome!=null)
            return daysIncome;

        int month = DataChecker.getCurrentMonth();
        int year = DataChecker.getCurrentYear();
        int n = DataChecker.numbOfDays(month, year);
        double[] days = new double[n];


        for(Transaction t : transactions)
            if( t.isInMonth(month, year) && t.getType().equals(TransactionType.INDIVIDUALINCOME))
                days[DataChecker.getDay(t.getDate())-1] += t.getAmount();
            else if(t.getType().equals(TransactionType.REGULARINCOME) && t.isBetween(month, year)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getDate());
                while(DataChecker.isBefore(cal.getTime(), t.getEndDate())){
                    if(DataChecker.isInMonthAndYear(cal.getTime(), month, year)){
                        days[DataChecker.getDay(cal.getTime())-1] += t.getAmount();
                    }
                    cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                }
            }

        daysIncome = days;
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
        if(monthsPayments!=null)
            return monthsPayments;

        int month = DataChecker.getCurrentMonth();
        int year = DataChecker.getCurrentYear();
        double[] months = new double[12];


        for(Transaction t : transactions)
            if( t.isInYear(year) && (t.getType().equals(TransactionType.INDIVIDUALPAYMENT) || t.getType().equals(TransactionType.PURCHASE)))
                months[DataChecker.getMonth(t.getDate())-1] += t.getAmount();
            else if(t.getType().equals(TransactionType.REGULARPAYMENT) && t.isBetween(month, year)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getDate());
                while(DataChecker.isBefore(cal.getTime(), t.getEndDate())){
                    if(DataChecker.isInYear(cal.getTime(), year)){
                        months[DataChecker.getMonth(cal.getTime())-1] += t.getAmount();
                    }
                    cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                }
            }

        monthsPayments = months;
        return months;
    }

    @Override
    public double[] getMonthsIncome() {
        if(monthsIncome!=null)
            return monthsIncome;

        int month = DataChecker.getCurrentMonth();
        int year = DataChecker.getCurrentYear();
        double[] months = new double[12];


        for(Transaction t : transactions)
            if( t.isInYear(year) && t.getType().equals(TransactionType.INDIVIDUALINCOME))
                months[DataChecker.getMonth(t.getDate())-1] += t.getAmount();
            else if(t.getType().equals(TransactionType.REGULARINCOME) && t.isBetween(month, year)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getDate());
                while(DataChecker.isBefore(cal.getTime(), t.getEndDate())){
                    if(DataChecker.isInYear(cal.getTime(), year)){
                        months[DataChecker.getMonth(cal.getTime())-1] += t.getAmount();
                    }
                    cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                }
            }

        monthsIncome = months;
        return months;
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

    @Override
    public double[] getWeeksPayments(){
        if(weeksPayments != null)
            return weeksPayments;

        Calendar cal1 = DataChecker.getFirstDayOfMonth(DataChecker.getCurrentMonth(), DataChecker.getCurrentYear());
        Calendar cal2 = DataChecker.getLastDayOfMonth(DataChecker.getCurrentMonth(), DataChecker.getCurrentYear());
        cal1.setFirstDayOfWeek(Calendar.MONDAY);
        cal2.setFirstDayOfWeek(Calendar.MONDAY);
        int w1 = cal1.get(Calendar.WEEK_OF_YEAR);
        int w2 = cal2.get(Calendar.WEEK_OF_YEAR);

        System.out.println(w1 + "   " + w2 + "------------------------------------------------------------------------");

        double[] weeks = new double[w2-w1+1];
        double[] days = getDayPayments();

        for(int i=0; i<days.length; i++){
            weeks[cal1.get(Calendar.WEEK_OF_YEAR) - w1] += days[i];
            cal1.add(Calendar.DAY_OF_MONTH, 1);
        }

        weeksPayments = weeks;
        return weeks;
    }

    @Override
    public double[] getWeeksIncome(){
        if(weeksIncome != null)
            return weeksIncome;

        Calendar cal1 = DataChecker.getFirstDayOfMonth(DataChecker.getCurrentMonth(), DataChecker.getCurrentYear());
        Calendar cal2 = DataChecker.getLastDayOfMonth(DataChecker.getCurrentMonth(), DataChecker.getCurrentYear());
        cal1.setFirstDayOfWeek(Calendar.MONDAY);
        cal2.setFirstDayOfWeek(Calendar.MONDAY);
        int w1 = cal1.get(Calendar.WEEK_OF_YEAR);
        int w2 = cal2.get(Calendar.WEEK_OF_YEAR);

        double[] weeks = new double[w2-w1+1];
        double[] days = getDayIncome();

        for(int i=0; i<days.length; i++){
            weeks[cal1.get(Calendar.WEEK_OF_YEAR) - w1] += days[i];
            cal1.add(Calendar.DAY_OF_MONTH, 1);
        }

        weeksIncome = weeks;
        return weeks;
    }

    @Override
    public double[] getWeeksAll(){
        double[] income = getWeeksIncome();
        double[] payments = getWeeksPayments();
        double[] all = new double[income.length];
        all[0] = income[0] - payments[0];

        for(int i=1; i<income.length; i++)
            all[i] = all[i-1] + income[i] + payments[i];

        return all;
    }

}
