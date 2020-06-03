package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.FrameLayout;

import ba.unsa.etf.rma.rma20hastornedim52.ConnectivityBroadcastReceiver;
import ba.unsa.etf.rma.rma20hastornedim52.R;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit.TransactionDetailFragment;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;

public class MainActivity extends AppCompatActivity
    implements TransactionListFragment.OnItemClick, MainMVP.ActivityFuncions {

    private boolean twoPaneMode = false;
    // Treba implementirati
    private boolean isConnected = false;

    TransactionDetailFragment detailFragment;
    TransactionListFragment listFragment;
    Fragment fragment;
    FragmentManager fragmentManager;

    private ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver();
    private IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TransactionType.getTransactionTypes();

        fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.edit_transaction_frame);
        if (details != null) {
            twoPaneMode = true;
            detailFragment = (TransactionDetailFragment)
                    fragmentManager.findFragmentById(R.id.edit_transaction_frame);
            if (detailFragment == null) {
                detailFragment = new TransactionDetailFragment();
                Bundle arguments = new Bundle();
                arguments.putParcelable("transaction", null);
                detailFragment.setArguments(arguments);
                fragmentManager.beginTransaction().
                        replace(R.id.edit_transaction_frame, detailFragment)
                        .commit();
            }
            onItemClicked(null);
        } else {
            twoPaneMode = false;
        }
        fragment = fragmentManager.findFragmentByTag("list");
        listFragment = (TransactionListFragment) fragment;
        if (fragment == null) {
            fragment = listFragment = new TransactionListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.transaction_list_frame, fragment, "list")
                    .commit();
        } else {
            fragmentManager.popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onItemClicked(Transaction transaction) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("transaction", transaction);
        detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.edit_transaction_frame, detailFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list_frame,detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {

        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(!twoPaneMode && detailFragment!=null)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void refreshList(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(listFragment);
        ft.attach(listFragment);
        ft.commit();
    }

    @Override
    public void refreshTransactions(){
        listFragment.getPresenter().refreshList();
    }

    @Override
    public boolean isTwoPaneMode() {
        return twoPaneMode;
    }

    @Override
    public int getCurrentMonth(){
        return listFragment.getCurrentMonth();
    }

    @Override
    public int getCurrentYear(){
        return listFragment.getCurrentYear();
    }

}
