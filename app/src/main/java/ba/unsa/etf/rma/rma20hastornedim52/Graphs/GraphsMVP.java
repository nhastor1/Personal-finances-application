package ba.unsa.etf.rma.rma20hastornedim52.Graphs;


import ba.unsa.etf.rma.rma20hastornedim52.Account;

public interface GraphsMVP {
    public interface View{
        GraphsMVP.Presenter getPresenter();
    }

    public interface Presenter{
        Account getAccount();
        void updateAccount(double monthLimit, double yearLimtit);
    }

    public interface Interactor{
        Account getAccount();
    }
}
