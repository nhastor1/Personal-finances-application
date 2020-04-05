package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import ba.unsa.etf.rma.rma20hastornedim52.R;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit.TransactionDetailFragment;

public class MainActivity extends AppCompatActivity
    implements TransactionListFragment.OnItemClick{

    private boolean twoPaneMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.edit_transaction_frame);
        if (details != null) {
            twoPaneMode = true;
            TransactionDetailFragment detailFragment = (TransactionDetailFragment)
                    fragmentManager.findFragmentById(R.id.edit_transaction_frame);
            if (detailFragment == null) {
                detailFragment = new TransactionDetailFragment();
                fragmentManager.beginTransaction().
                        replace(R.id.edit_transaction_frame, detailFragment)
                        .commit();
            }
        } else {
            twoPaneMode = false;
        }
        Fragment listFragment = fragmentManager.findFragmentByTag("list");
        if (listFragment == null) {
            listFragment = new TransactionListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.transaction_list_frame, listFragment, "list")
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
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.edit_transaction_frame, detailFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.edit_transaction_frame,detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


}
