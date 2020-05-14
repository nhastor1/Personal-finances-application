package ba.unsa.etf.rma.rma20hastornedim52;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetTransactionType extends AsyncTask<String, Integer, Void> {

    private final String LINK = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private final String KEY = "65483f4c-0f46-474f-bec7-de0cb02670d6";

    private Map<Integer, TransactionType> map = new HashMap<>();

    @Override
    protected Void doInBackground(String... strings) {
        // GetAccount
        String url1 = LINK + "transactionTypes";
        try {
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection)
                        url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = JSONFunctions.convertStreamToString(in);

                JSONObject jsonObject = (new JSONObject(result));
                int count = jsonObject.getInt("count");

                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                for(int i=0; i<count; i++){

                    JSONObject jo = (JSONObject) jsonArray.get(i);

                    int id = jo.getInt("id");
                    String name = jo.getString("name");
                    TransactionType type = TransactionType.getType(name);

                    map.put(id, type);

                }

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
        TransactionType.map = map;
    }
}
