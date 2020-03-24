package ba.unsa.etf.rma.rma20hastornedim52;

import java.util.List;

public interface MainMVP {
    public interface Interactor{
        public List<Transaction> get();
    }

    public interface Presenter{

    }

    public interface  View{
        public void setTransactions(List<Transaction> transactions);
        public void notifyMovieListDataSetChanged();
    }
}