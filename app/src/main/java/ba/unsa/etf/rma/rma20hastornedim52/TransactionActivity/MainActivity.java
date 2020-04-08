package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import ba.unsa.etf.rma.rma20hastornedim52.R;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit.TransactionDetailFragment;

public class MainActivity extends AppCompatActivity
    implements TransactionListFragment.OnItemClick, MainMVP.RefreshListFragment{

    private boolean twoPaneMode = false;

    TransactionDetailFragment detailFragment;
    TransactionListFragment listFragment;
    Fragment fragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.edit_transaction_frame);
        if (details != null) {
            twoPaneMode = true;
            detailFragment = (TransactionDetailFragment)
                    fragmentManager.findFragmentById(R.id.edit_transaction_frame);
            if (detailFragment == null) {
                detailFragment = new TransactionDetailFragment();
                Bundle arguments = new Bundle();
                arguments.putBoolean("twoPanelMode", twoPaneMode);
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
        arguments.putBoolean("twoPanelMode", twoPaneMode);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void refreshList(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(listFragment);
        ft.attach(listFragment);
        ft.commit();
    }
}
