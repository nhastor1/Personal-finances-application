package ba.unsa.etf.rma.rma20hastornedim52;

import java.util.List;

public class TransactionPresenter {
    private MainActivity mainActivity;
    private Account account;
    private List<Transaction> transactions = TransactionModel.transactions;

    public TransactionPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        account = new Account(500000, 1000000, 100000);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
