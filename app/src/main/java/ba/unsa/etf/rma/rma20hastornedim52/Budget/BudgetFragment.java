package ba.unsa.etf.rma.rma20hastornedim52.Budget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import ba.unsa.etf.rma.rma20hastornedim52.ConnectivityBroadcastReceiver;
import ba.unsa.etf.rma.rma20hastornedim52.Graphs.GraphsFragment;
import ba.unsa.etf.rma.rma20hastornedim52.R;
import ba.unsa.etf.rma.rma20hastornedim52.OnSwipeTouchListener;

public class BudgetFragment extends Fragment implements BudgetMVP.View {

    public static BudgetFragment budgetFragment;

    private BudgetMVP.Presenter presenter;
    private View view;
    private EditText editTextMonthlimit;
    private EditText editTextYearLimit;
    private TextView textViewTotalAmount;
    private Button buttonSaveBudget;
    private TextView textViewOfflineModeBudget;

    @Override
    public BudgetMVP.Presenter getPresenter(){
        if(presenter==null)
            presenter = new BudgetPresenter(this, getContext());
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_budget, container, false);

        getPresenter();

        editTextMonthlimit = (EditText) view.findViewById(R.id.editTextMonthLimit);
        editTextYearLimit = (EditText) view.findViewById(R.id.editTextYearLimit);
        textViewTotalAmount = (TextView) view.findViewById(R.id.textViewTotalAmount);
        buttonSaveBudget = (Button) view.findViewById(R.id.buttonSaveBudget);
        textViewOfflineModeBudget = (TextView) view.findViewById(R.id.textViewOflineModeBudget);

        buttonSaveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double monthLimit = Double.parseDouble(editTextMonthlimit.getText().toString());
                    double yearLimit = Double.parseDouble(editTextYearLimit.getText().toString());
                    getPresenter().updateAccount(monthLimit, yearLimit);
                }catch (NumberFormatException e){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle(R.string.incorrect_data);
                    alertDialog.setMessage("You need to enter real number in those fields");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }

                finish();
            }
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeRight() {
                BudgetFragment.budgetFragment = null;
                finish();
            }

            @Override
            public void onSwipeLeft() {
                BudgetFragment.budgetFragment = null;
                finish();
                Fragment fragment = new GraphsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.transaction_list_frame, fragment).addToBackStack(null)
                        .commit();
            }
        });

        offlineMode();

        return view;
    }

    @Override
    public void refresh(){
        try {
            editTextMonthlimit.setText(getString(R.string.double_to_string, getPresenter().getAccount().getMonthLimit()));
            editTextYearLimit.setText(getString(R.string.double_to_string, getPresenter().getAccount().getTotalLimit()));
            textViewTotalAmount.setText(getString(R.string.double_to_string, getPresenter().getAccount().getBudget()));
        }
        catch (IllegalStateException e){
            // If fragment is not attached do nothing
        }
    }

    public void offlineMode(){
        if(ConnectivityBroadcastReceiver.isConnected){
            textViewOfflineModeBudget.setText("");
        }
        else{
            textViewOfflineModeBudget.setText(R.string.offline_editing);
        }
    }

    private void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
