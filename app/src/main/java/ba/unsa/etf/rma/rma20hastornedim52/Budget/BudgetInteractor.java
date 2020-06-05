package ba.unsa.etf.rma.rma20hastornedim52.Budget;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

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

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.ConnectivityBroadcastReceiver;
import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.JSONFunctions;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionDBOpenHelper;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;

public class BudgetInteractor extends AsyncTask<String, Integer, Void> implements BudgetMVP.Interactor {

    private final String LINK = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private final String KEY = "65483f4c-0f46-474f-bec7-de0cb02670d6";

    private BudgetInteractor.OnAccountSearchDone caller;
    private Account account;
    private Context context;
    private boolean callAfterExecution = false;

    public BudgetInteractor(OnAccountSearchDone caller, Context context) {
        this.caller = caller;
        this.context = context;
    }

    public interface OnAccountSearchDone{
        void onDone(Account account);
    }

    @Override
    protected Void doInBackground(String... strings) {
        if(ConnectivityBroadcastReceiver.isConnected) {
            if (strings.length == 0) {
                callAfterExecution = true;
                // GetAccount
                String url1 = LINK + "account/" + KEY;
                try {
                    URL url = new URL(url1);
                    HttpURLConnection urlConnection = (HttpURLConnection)
                            url.openConnection();

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String result = JSONFunctions.convertStreamToString(in);

                    JSONObject jo = new JSONObject(result);

                    int id = jo.getInt("id");
                    double budget = jo.getDouble("budget");
                    double totalLimit = jo.getDouble("totalLimit");
                    double monthLimit = jo.getDouble("monthLimit");

                    account = new Account(id, budget, totalLimit, monthLimit);
                    TransactionModel.account = account;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (strings[0].equals("Update")) {
                callAfterExecution = false;
                // Update account
                String url1 = LINK + "account/" + KEY;
                try {
                    URL url = new URL(url1);
                    HttpURLConnection con = (HttpURLConnection)
                            url.openConnection();

                    // For post method
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    con.setRequestProperty("Accept", "application/json");
                    con.setDoOutput(true);

                    String jsonInputString;
                    if (strings.length == 3)
                        jsonInputString = "{\"monthLimit\":" + strings[1] + ", \"totalLimit\":" + strings[2] + "}";
                    else
                        jsonInputString = "{\"budget\":" + strings[1] + "}";

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
        else{
            if (strings.length > 0 && strings[0].equals("Update")){
                callAfterExecution = false;
                if (strings.length == 3){
                    double monthLimit = Double.parseDouble(strings[1]);
                    double totalLimit = Double.parseDouble(strings[2]);
                    TransactionModel.account.setMonthLimit(monthLimit);
                    TransactionModel.account.setTotalLimit(totalLimit);
                }
                else {
                    double budget = Double.parseDouble(strings[1]);
                    TransactionModel.account.setBudget(budget);
                }

                // Adding to database
                ContentResolver cr = context.getApplicationContext().getContentResolver();
                Uri uri = Uri.parse("content://rma.provider.accounts/elements");
                ContentValues values = new ContentValues();
                values.put(TransactionDBOpenHelper.ACCOUNT_ID, TransactionModel.account.getId());
                values.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, TransactionModel.account.getMonthLimit());
                values.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, TransactionModel.account.getTotalLimit());
                values.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, TransactionModel.account.getBudget());
                cr.delete(ContentUris.withAppendedId(uri, TransactionModel.account.getId()),null,null);
                cr.insert(uri, values);
            }
            else{
                callAfterExecution = true;
            }
            account = TransactionModel.account;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        if(callAfterExecution)
            caller.onDone(account);
    }

    @Override
    public Account getAccount(){
        return TransactionModel.account;
    }

    @Override
    public void updateAccount(Account account){
        TransactionModel.account = account;
    }
}
