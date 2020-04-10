package ba.unsa.etf.rma.rma20hastornedim52.Budget;

import android.content.Context;

import ba.unsa.etf.rma.rma20hastornedim52.Account;

public class BudgetPresenter implements BudgetMVP.Presenter {

    private Context context;
    private BudgetMVP.Interactor interactor;
    private BudgetMVP.View view;
    private Account account;

    public BudgetPresenter(BudgetMVP.View view, Context context) {
        this.context = context;
        this.interactor = new BudgetInteractor();
        this.view = view;
        account = interactor.getAccount();
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
}
