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
        this.interactor = new BudgetInteractor((BudgetInteractor.OnAccountSearchDone) this, context);
        this.view = view;

        (new BudgetInteractor(this, context)).execute();
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void updateAccount(double monthLimit, double yearLimtit){
        (new BudgetInteractor(this, context)).execute("Update", Double.toString(monthLimit), Double.toString(yearLimtit));
    }

    @Override
    public void onDone(Account account) {
        this.account = account;
        view.refresh();
    }
}
