package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;

import android.os.AsyncTask;
import android.view.SurfaceControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.JSONFunctions;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;

public class TransactionEditInteractor extends AsyncTask<String, Integer, Void> implements TransactionEditMVP.Interactor {

    private final String LINK = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private final String KEY = "65483f4c-0f46-474f-bec7-de0cb02670d6";

    private TransactionEditInteractor.OnTransactionEditDone caller1;
    private TransactionEditInteractor.OnTransactionRemoveDone caller2;
    private TransactionEditInteractor.OnTransactionAddDone caller3;
    private TransactionEditMVP.Presenter presenter;
    private Account account;
    private Transaction transaction;

    public TransactionEditInteractor(TransactionEditMVP.Presenter p, Transaction transaction) {
        this.transaction = transaction;
        this.presenter = p;
    }

    public TransactionEditInteractor(OnTransactionEditDone caller, Transaction transaction) {
        this.transaction = transaction;
        this.caller1 = caller;
    }

    public TransactionEditInteractor(OnTransactionRemoveDone caller, Transaction transaction) {
        this.transaction = transaction;
        this.caller2 = caller;
    }

    public TransactionEditInteractor(OnTransactionAddDone caller, Transaction transaction) {
        this.transaction = transaction;
        this.caller3 = caller;
    }

    public interface OnTransactionEditDone{
        void onEditDone(Transaction transaction);
    }

    public interface OnTransactionRemoveDone{
        void onRemoveDone(Transaction transaction);
    }

    public interface OnTransactionAddDone{
        void onAddDone(Transaction transaction);
    }

    @Override
    protected Void doInBackground(String... strings) {
        if(caller2!=null){
            // Remove Transaction
            String url1 = LINK + "account/" + KEY + "/transactions/" + transaction.getId();
            try {
                URL url = new URL(url1);
                HttpURLConnection con = (HttpURLConnection)
                        url.openConnection();

                // For post method
                con.setRequestMethod("DELETE");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                System.out.println(con.getResponseCode());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            // Edit transactions
            String url1 = LINK + "account/" + KEY + "/transactions";

            // Add transactions
            if(caller1!=null)
                url1 += "/" + transaction.getId();

            System.out.println(url1);

            try {
                URL url = new URL(url1);
                HttpURLConnection con = (HttpURLConnection)
                        url.openConnection();

                // For post method
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                String jsonInputString = "{\"date\":\"" + DataChecker.getStringDateForService(transaction.getDate()) +
                        "\", \"title\":\"" + transaction.getTitle() + "\", " +
                        "\"amount\":" + transaction.getAmount() +
                        ", \"endDate\":\"" + DataChecker.getStringDateForService(transaction.getEndDate()) + "\", " +
                        "\"itemDescription\":\"" + transaction.getItemDescription() +
                        "\", \"transactionInterval\":" + transaction.getTransactionInterval() + ", " +
                        "\"typeId\":" + TransactionType.getId(transaction.getType()) + "}";

                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        if(caller1!=null )
            caller1.onEditDone(transaction);
        else if(caller2!=null)
            caller2.onRemoveDone(transaction);
        else
            caller3.onAddDone(transaction);
    }


    @Override
    public List<Transaction> getTransactions() {
        return TransactionModel.transactions;
    }

    @Override
    public Account getAccount() {
        return TransactionModel.account;
    }

    @Override
    public void removeTransaction(Transaction transaction){
        TransactionModel.transactions.remove(transaction);
    }
}
