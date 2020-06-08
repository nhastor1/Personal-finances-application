package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity.MainMVP;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    public static boolean isConnected = false;
    public static MainMVP.ActivityFuncions mainActivity;

    public ConnectivityBroadcastReceiver(MainMVP.ActivityFuncions activity){
        mainActivity = activity;
    }

    public ConnectivityBroadcastReceiver(){
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(context, "Offline mode", Toast.LENGTH_SHORT);
            toast.show();

            isConnected = false;
            mainActivity.addOfflineMode();
            Log.e("Connectivity", "Disconnected");
        }
        else {
            Toast toast = Toast.makeText(context, "Online mode", Toast.LENGTH_SHORT);
            toast.show();

            new UpdateService(context, false);
            isConnected = true;
            mainActivity.removeOfflineMode();
            Log.e("Connectivity", "Connected");
        }
    }
}
