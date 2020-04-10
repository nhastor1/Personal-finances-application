package ba.unsa.etf.rma.rma20hastornedim52.Budget;

import ba.unsa.etf.rma.rma20hastornedim52.Account;

public interface BudgetMVP {
    public interface View{

        Presenter getPresenter();
    }

    public interface Presenter{

        Account getAccount();

        void updateAccount(double monthLimit, double yearLimtit);
    }

    public interface Interactor{

        Account getAccount();

        void updateAccount(Account account);
    }
}
