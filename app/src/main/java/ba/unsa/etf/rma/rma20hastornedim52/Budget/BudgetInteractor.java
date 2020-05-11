package ba.unsa.etf.rma.rma20hastornedim52.Budget;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ba.unsa.etf.rma.rma20hastornedim52.Account;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionModel;

public class BudgetInteractor extends AsyncTask<String, Integer, Void> implements BudgetMVP.Interactor {

    private final String LINK = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private final String KEY = "65483f4c-0f46-474f-bec7-de0cb02670d6";

    private BudgetInteractor.OnAccountSearchDone caller;
    private Account account;

    public BudgetInteractor(OnAccountSearchDone caller) {
        this.caller = caller;
    }

    public interface OnAccountSearchDone{
        void onDone(Account account);
    }

    @Override
    protected Void doInBackground(String... strings) {
        // GetAccount
        String url1 = LINK + "account/" + KEY;
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection)
                    url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);

            JSONObject jo = new JSONObject(result);

            int id = jo.getInt("id");
            double budget = jo.getDouble("budget");
            double totalLimit = jo.getDouble("totalLimit");
            double monthLimit = jo.getDouble("monthLimit");

            account = new Account(id, budget, totalLimit, monthLimit);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
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

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            //
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                //
            }
        }
        return sb.toString();
    }
}
