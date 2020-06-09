package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceControl;

import androidx.core.graphics.drawable.IconCompat;

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
import ba.unsa.etf.rma.rma20hastornedim52.ConnectivityBroadcastReceiver;
import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.JSONFunctions;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionDBOpenHelper;
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
    private Context context;

    public TransactionEditInteractor(TransactionEditMVP.Presenter p, Transaction transaction, Context context) {
        this.transaction = transaction;
        this.presenter = p;
        this.context = context;
    }

    public TransactionEditInteractor(OnTransactionEditDone caller, Transaction transaction, Context context) {
        this.transaction = transaction;
        this.caller1 = caller;
        this.context = context;
    }

    public TransactionEditInteractor(OnTransactionRemoveDone caller, Transaction transaction, Context context) {
        this.transaction = transaction;
        this.caller2 = caller;
        this.context = context;
    }

    public TransactionEditInteractor(OnTransactionAddDone caller, Transaction transaction, Context context) {
        this.transaction = transaction;
        this.caller3 = caller;
        this.context = context;
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
        if(ConnectivityBroadcastReceiver.isConnected) {
            if (caller2 != null) {
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
            } else {
                // Add transactions
                Log.e("A_T", "Da dodaje se");
                String url1 = LINK + "account/" + KEY + "/transactions";

                // Edit transactions
                if (caller1 != null)
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
                            "\"TransactionTypeId\":" + TransactionType.getId(transaction.getType()) + "}";

                    try (OutputStream os = con.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    try (BufferedReader br = new BufferedReader(
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
        }
        else {
            ContentResolver cr = context.getApplicationContext().getContentResolver();
            Uri uri = Uri.parse("content://rma.provider.transactions/elements");
            ContentValues values = new ContentValues();
            values.put(TransactionDBOpenHelper.TRANSACTION_ID, transaction.getId());
            values.put(TransactionDBOpenHelper.TRANSACTION_DATE, DataChecker.getStringDateForService(transaction.getDate()));
            values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount());
            values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
            values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, TransactionType.getId(transaction.getType()));
            values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
            if (TransactionType.isRegular(transaction.getType())) {
                values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, DataChecker.getStringDateForService(transaction.getEndDate()));
            } else {
                values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, "");
            }
            values.put(TransactionDBOpenHelper.TRANSACTION_ORGINAL_AMOUNT, transaction.getOrginalAmount());

            if (caller2 != null) {
                // Remove transaction
                //TransactionModel.transactions.remove(transaction);

                values.put(TransactionDBOpenHelper.TRANSACTION_CHANGE, TransactionDBOpenHelper.TRANSACTION_MODE_REMOVE);
                cr.insert(uri, values);
            }
            else if (caller3 != null) {
                // Adding transaction
                TransactionModel.transactions.add(transaction);

                values.put(TransactionDBOpenHelper.TRANSACTION_CHANGE, TransactionDBOpenHelper.TRANSACTION_MODE_ADD);
                cr.insert(uri, values);
            }
            else {
                // Editing transaction
                values.put(TransactionDBOpenHelper.TRANSACTION_CHANGE, TransactionDBOpenHelper.TRANSACTION_MODE_EDIT);
                cr.insert(uri, values);
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
