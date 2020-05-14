package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import android.os.AsyncTask;

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
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Budget.BudgetInteractor;
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

    private List<Transaction> transactions = new ArrayList<>();


    public TransactionInteractor(MainMVP.Presenter p) {
        presenter = p;
        (new BudgetInteractor(this)).execute();
    }

    public TransactionInteractor(OnTransactionsSearchDone p) {
        caller = p;
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
        // GetAccount
        String url1 = LINK + "account/" + KEY + "/transactions?page=";
        try {

            boolean notFinish = true;
            for(int page=0; notFinish; page++) {

                URL url = new URL(url1 + page);
                HttpURLConnection urlConnection = (HttpURLConnection)
                        url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = JSONFunctions.convertStreamToString(in);

                JSONArray jsonArray = (new JSONObject(result)).getJSONArray("transactions");

                for(int i=0; i<5; i++){

                    JSONObject jo = (JSONObject) jsonArray.get(i);

                    if(jo == null){
                        notFinish = false;
                        break;
                    }

                    int id = jo.getInt("id");
                    String date = jo.getString("date");
                    String title = jo.getString("title");
                    double amount = jo.getDouble("amount");
                    String itemDescription = jo.optString("itemDescription", "");
                    if(itemDescription.equals("")) itemDescription = null;
                    int transactionInterval = jo.optInt("transactionInterval", 0);
                    String endDate = jo.optString("endDate", "");
                    if(endDate.equals("")) endDate = null;
                    int accountId = jo.getInt("AccountId");
                    int transactionTypeId = jo.getInt("TransactionTypeId");

                    System.out.println("-----------------------------8------------------------------------------");

                    Transaction t = new Transaction();
                    t.setId(id);
                    t.setType(TransactionType.getType(transactionTypeId));
                    System.out.println("-----------------------------9------------------------------------------");
                    t.setDate(null);
                    t.setTitle(title);
                    t.setAmount(amount);
                    t.setItemDescription(itemDescription);
                    t.setTransactionInterval(transactionInterval);
                    t.setEndDate(null);

                    System.out.println("-----------------------------7------------------------------------------");
                    transactions.add(t);
                }

                System.out.println("-----------------------------6------------------------------------------");

            }

            System.out.println("-----------------------------5------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        System.out.println("-----------------------------4------------------------------------------");
        super.onPostExecute(aVoid);
        caller.onDone(transactions);
    }

    @Override
    public List<Transaction> getTransactions(){
        return transactions;
    }

    @Override
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
