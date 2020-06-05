package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Budget.BudgetInteractor;
import ba.unsa.etf.rma.rma20hastornedim52.ConnectivityBroadcastReceiver;
import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.JSONFunctions;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;

public class TransactionInteractor extends AsyncTask<String, Integer, Void> implements MainMVP.Interactor, BudgetInteractor.OnAccountSearchDone {

    private final String LINK = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private final String KEY = "65483f4c-0f46-474f-bec7-de0cb02670d6";

    private MainMVP.Presenter presenter;
    private OnTransactionsSearchDone caller;
    private Account account;
    private Context context;

    private List<Transaction> transactions = new ArrayList<>();


    public TransactionInteractor(MainMVP.Presenter p, Context context) {
        presenter = p;
        this.context = context;

        (new BudgetInteractor(this, context)).execute();
    }

    public TransactionInteractor(OnTransactionsSearchDone caller, MainMVP.Presenter presenter, Context context) {
        this.presenter = presenter;
        this.caller = caller;
        this.context = context;
    }

    @Override
    public void onDone(Account account) {
        presenter.onDone(account);
    }

    public interface OnTransactionsSearchDone{
        void onDone(List<Transaction> transactions);
    }

    @Override
    protected Void doInBackground(String... strings) {
        if(ConnectivityBroadcastReceiver.isConnected) {
            // Get Transactions
            String url1 = LINK + "account/" + KEY + "/transactions?page=";
            Log.e("TAG", url1 + "  OOOOOJ");
            try {

                boolean notFinish = true;
                for (int page = 0; notFinish; page++) {

                    URL url = new URL(url1 + page);
                    HttpURLConnection urlConnection = (HttpURLConnection)
                            url.openConnection();

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String result = JSONFunctions.convertStreamToString(in);

                    JSONArray jsonArray = (new JSONObject(result)).getJSONArray("transactions");

                    for (int i = 0; i < 5; i++) {

                        JSONObject jo = (JSONObject) jsonArray.get(i);

                        if (jo == null) {
                            notFinish = false;
                            break;
                        }

                        int id = jo.getInt("id");
                        String date = jo.getString("date");
                        String title = jo.getString("title");
                        double amount = jo.getDouble("amount");
                        String itemDescription = jo.optString("itemDescription", "");
                        if (itemDescription.equals("")) itemDescription = null;
                        int transactionInterval = jo.optInt("transactionInterval", 0);
                        String endDate = jo.optString("endDate", "");
                        if (endDate.equals("")) endDate = null;
                        int accountId = jo.getInt("AccountId");
                        int transactionTypeId = jo.getInt("TransactionTypeId");


                        Transaction t = new Transaction();
                        t.setId(id);
                        t.setType(TransactionType.getType(transactionTypeId));
                        t.setDate(DataChecker.getDateFromService(date));
                        t.setTitle(title);
                        t.setAmount(amount);
                        t.setItemDescription(itemDescription);
                        t.setTransactionInterval(transactionInterval);
                        if (endDate != null && !endDate.equals(""))
                            t.setEndDate(DataChecker.getDateFromService(endDate));

                        transactions.add(t);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                if(ConnectivityBroadcastReceiver.isConnected)
                    TransactionModel.transactions = transactions;
                else if(TransactionModel.transactions!=null)
                    transactions = TransactionModel.transactions;
            }
        }
        else if(TransactionModel.transactions!=null){
            transactions = TransactionModel.transactions;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        Log.e("MOLIM TE", transactions.size() + "");
        caller.onDone(transactions);
        (new BudgetInteractor((BudgetInteractor.OnAccountSearchDone) this, context)).execute();
    }

    @Override
    public List<Transaction> getTransactions(){
        return new ArrayList<>(transactions);
    }

    @Override
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
