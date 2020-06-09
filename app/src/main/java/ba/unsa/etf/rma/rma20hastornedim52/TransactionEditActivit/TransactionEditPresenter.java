package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.SurfaceControl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Budget.BudgetInteractor;
import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity.TransactionInteractor;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;

public class TransactionEditPresenter implements TransactionEditMVP.Presenter,
    TransactionEditInteractor.OnTransactionAddDone, TransactionEditInteractor.OnTransactionRemoveDone,
    TransactionEditInteractor.OnTransactionEditDone, BudgetInteractor.OnAccountSearchDone {

    private Context context;
    private TransactionEditMVP.View view;
    private TransactionEditMVP.Interactor interactor;
    private Transaction transaction;
    private boolean undo = false;

    private double budgetChange = 0;

    public TransactionEditPresenter(TransactionEditMVP.View view, Context context, Transaction transaction) {
        this.context = context;
        this.view = view;
        this.transaction = transaction;
        interactor = new TransactionEditInteractor( (TransactionEditMVP.Presenter) this, transaction, context);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void updatedBudget(double budgetChange) {
        this.budgetChange = budgetChange;
        (new BudgetInteractor( (BudgetInteractor.OnAccountSearchDone) this, context)).execute();
    }

    @Override
    public double getTotalAmount() {
        if(interactor.getAccount()==null)
            return 0;
        return interactor.getAccount().getBudget();
    }

    @Override
    public double getMonthAmount(Date date) {
        List<Transaction> trans = interactor.getTransactions();
        double total = 0;

        int month = DataChecker.getMonth(date);
        int year = DataChecker.getYear(date);

        for (Transaction t : trans)
            if (!(t.getType().equals(TransactionType.REGULARINCOME) || t.getType().equals(TransactionType.REGULARPAYMENT))) {
                if (DataChecker.getMonth(date) == month &&
                        DataChecker.getYear(date) == year)
                    if(t.getType().equals(TransactionType.INDIVIDUALINCOME))
                        total += t.getAmount();
                    else
                        total -= t.getAmount();

            } else {
                    Calendar cal = Calendar.getInstance();
                    Calendar calEnd = Calendar.getInstance();
                    cal.setTime(t.getDate());
                    calEnd.setTime(t.getEndDate());

                    while (DataChecker.isBefore(cal.getTime(), calEnd.getTime())){
                        if(t.getType().equals(TransactionType.REGULARPAYMENT))
                            total -= t.getAmount();
                        else
                            total += t.getAmount();
                        cal.add(Calendar.DAY_OF_MONTH, t.getTransactionInterval());
                    }
            }

        return total;
    }

    @Override
    public Transaction getTransaction(int id){
        return interactor.getTransactions().get(id);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionAddDone) this, transaction, context)).execute();
    }

    @Override
    public void editTransaction(Transaction transaction) {
        (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionEditDone) this, transaction, context)).execute();
    }

    @Override
    public void removeTransaction(Transaction transaction) {
        (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionRemoveDone) this, transaction, context)).execute();
        double budgetChange;

        if(transaction.getOrginalAmount()!=0)
            budgetChange = transaction.getOrginalAmount();
        else
            budgetChange = transaction.getTotalAmount();

        updatedBudget(-budgetChange);
    }

    @Override
    public void undoOfflineTransaction(Transaction transaction) {
        undo = true;
        (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionAddDone) this, transaction, context)).execute();
        double budgetChange = transaction.getTotalAmount();

        if(!TransactionType.isIncome(transaction.getType()))
            budgetChange = -budgetChange;

        updatedBudget(budgetChange);
    }

    @Override
    public Account getAccount() {
        return interactor.getAccount();
    }

    @Override
    public void onAddDone(Transaction transaction) {
        if(undo) {
            TransactionModel.transactions.remove(TransactionModel.transactions.size() - 1);
            undo = false;
        }
    }

    @Override
    public void onEditDone(Transaction transaction) {
        // Edit transaction in list
    }

    @Override
    public void onRemoveDone(Transaction transaction) {
        // Remove transaction from list
    }

    @Override
    public void onDone(Account account) {
        budgetChange = account.getBudget() + budgetChange;
        (new BudgetInteractor( (BudgetInteractor.OnAccountSearchDone) this, context)).execute("Update", Double.toString(budgetChange));
    }
}
