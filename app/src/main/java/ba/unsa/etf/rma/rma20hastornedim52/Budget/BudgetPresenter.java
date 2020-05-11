package ba.unsa.etf.rma.rma20hastornedim52.Budget;

import android.content.Context;
import android.os.AsyncTask;

import ba.unsa.etf.rma.rma20hastornedim52.Account;

public class BudgetPresenter implements BudgetMVP.Presenter, BudgetInteractor.OnAccountSearchDone {

    private Context context;
    private BudgetMVP.Interactor interactor;
    private BudgetMVP.View view;
    private Account account;

    public BudgetPresenter(BudgetMVP.View view, Context context) {
        this.context = context;
        this.interactor = new BudgetInteractor((BudgetInteractor.OnAccountSearchDone) this);
        this.view = view;

        ((AsyncTask<String, Integer, Void>) interactor).execute();
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void updateAccount(double monthLimit, double yearLimtit){
        account.setMonthLimit(monthLimit);
        account.setTotalLimit(yearLimtit);
        interactor.updateAccount(account);
    }

    @Override
    public void onDone(Account account) {
        this.account = account;
        view.refresh();
    }
}
