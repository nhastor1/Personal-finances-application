package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Adapter.FilterSpinnerAdapter;
import ba.unsa.etf.rma.rma20hastornedim52.ConnectivityBroadcastReceiver;
import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
import ba.unsa.etf.rma.rma20hastornedim52.R;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity.MainMVP;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;

public class TransactionDetailFragment extends Fragment implements TransactionEditMVP.View {

    View view;
    private TransactionEditMVP.Presenter presenter;

    private Transaction transaction;

    private EditText editTextEndDateEdit;

    private EditText editTextTitle;
    private EditText editTextAmount;
    private EditText editTextDescription;
    private EditText editTextDate;
    private EditText editTextTransactionInterval;
    private TextView textViewOfflineMode;

    private Spinner spinnerTransactionType;

    private Button buttonSave;
    private Button buttonDelete;

    private String activity;

    public TransactionEditMVP.Presenter getPresenter() {
        if (presenter == null) {
            presenter = new TransactionEditPresenter(this,getActivity().getApplicationContext(), transaction);
        }
        return presenter;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction_edit, container, false);


        if (getArguments() != null && getArguments().containsKey("transaction")) {
            getPresenter().setTransaction((Transaction) getArguments().getParcelable("transaction"));
            transaction = getPresenter().getTransaction();

            if(transaction==null)
                activity = "add";
            else {
                activity = "edit";
            }

            textViewOfflineMode = (TextView) view.findViewById(R.id.textViewOflineMode);

            if (activity.equals("edit")) {
                ((TextView) view.findViewById(R.id.textViewEditOrAdd)).setText(R.string.edit_transaction);
                if(!ConnectivityBroadcastReceiver.isConnected)
                    ((TextView) view.findViewById(R.id.textViewOflineMode)).setText(R.string.offline_editing);
                else
                    ((TextView) view.findViewById(R.id.textViewOflineMode)).setText("");
            } else {
                ((TextView) view.findViewById(R.id.textViewEditOrAdd)).setText(R.string.add_transaction);
                if(!ConnectivityBroadcastReceiver.isConnected)
                    addOfflineMode();
                else
                    removeOfflineMode();
                transaction = new Transaction(new Date(), 0, "Transaction", TransactionType.INDIVIDUALINCOME,
                        "");
            }

            editTextEndDateEdit = (EditText) view.findViewById(R.id.editTextEndDateEdit);

            editTextTitle = (EditText) view.findViewById(R.id.editTextTitle);
            editTextAmount = (EditText) view.findViewById(R.id.editTextAmount);
            editTextDescription = (EditText) view.findViewById(R.id.editTextDescription);
            editTextDate = (EditText) view.findViewById(R.id.editTextDate);
            editTextTransactionInterval = (EditText) view.findViewById(R.id.editTextTransactionInterval);

            spinnerTransactionType = (Spinner) view.findViewById(R.id.spinnerTransactionType);

            buttonSave = (Button) view.findViewById(R.id.buttonSave);
            buttonDelete = (Button) view.findViewById(R.id.buttonDelete);

            buttonSave.setText(R.string.save);
            buttonDelete.setText(R.string.delete);

            editTextTitle.setText(transaction.getTitle());
            editTextAmount.setText(getString(R.string.double_to_string, transaction.getAmount()));
            if(transaction.getItemDescription()==null)
                editTextDescription.setText("");
            else
                editTextDescription.setText(transaction.getItemDescription());
            editTextTransactionInterval.setText(getString(R.string.int_to_string, transaction.getTransactionInterval()));

            editTextDate.setText(DateFormat.format("dd.MM.yyyy", transaction.getDate().getTime()));

            if (!(transaction.getType().equals(TransactionType.REGULARPAYMENT) || transaction.getType().equals(TransactionType.REGULARINCOME)))
                editTextEndDateEdit.setText("");
            else {
                editTextEndDateEdit.setText(DateFormat.format("dd.MM.yyyy", transaction.getEndDate().getTime()));
            }

            if (activity.equals("edit")) {
                // Setting buttonDelete
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        if(buttonDelete.getText().toString().equals("Delete"))
                            builder.setMessage(R.string.dialog_message_delete).setTitle(R.string.dialog_title);
                        else
                            builder.setMessage(R.string.dialog_message_undo).setTitle(R.string.dialog_title);

                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(buttonDelete.getText().toString().equals("Delete")) {
                                    getPresenter().removeTransaction(transaction);
                                    if (ConnectivityBroadcastReceiver.isConnected) {
                                        ((MainMVP.ActivityFuncions) getActivity()).refreshTransactions();
                                        finish();
                                    } else {
                                        transaction.setDeleted(true);
                                        enableTransactionEdit(false);
                                    }
                                }else{
                                    transaction.setDeleted(false);
                                    enableTransactionEdit(true);
                                    getPresenter().undoOfflineTransaction(transaction);
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                        builder.create().show();
                    }
                });
            } else {
                buttonDelete.setEnabled(false);
            }

            // Setting buttonSave
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveChanges();
                }
            });

            // Setting spinnerFilter
            List<String> types = new ArrayList<String>() {
                {
                    add("INDIVIDUALPAYMENT");
                    add("REGULARPAYMENT");
                    add("INDIVIDUALINCOME");
                    add("REGULARINCOME");
                    add("PURCHASE");
                }
            };
            FilterSpinnerAdapter transactionTypeAdapter = new FilterSpinnerAdapter(getActivity().getApplicationContext(),
                    R.layout.spinner_filter_element, types);
            transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTransactionType.setAdapter(transactionTypeAdapter);

            switch (transaction.getType()) {
                case INDIVIDUALPAYMENT:
                    spinnerTransactionType.setSelection(0);
                    break;
                case REGULARPAYMENT:
                    spinnerTransactionType.setSelection(1);
                    break;
                case INDIVIDUALINCOME:
                    spinnerTransactionType.setSelection(2);
                    break;
                case REGULARINCOME:
                    spinnerTransactionType.setSelection(3);
                    break;
                default:
                    spinnerTransactionType.setSelection(4);
            }


            // On text change listeners

            editTextTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editTextTitle.getText().toString().equals(transaction.getTitle()))
                        editTextTitle.setBackgroundColor(getResources().getColor(R.color.no_color));
                    else
                        editTextTitle.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editTextAmount.getText().toString().equals(getString(R.string.double_to_string, transaction.getAmount())))
                        editTextAmount.setBackgroundColor(getResources().getColor(R.color.no_color));
                    else
                        editTextAmount.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editTextDescription.getText().toString().equals(transaction.getItemDescription()) ||
                            (editTextDescription.getText().toString().equals("") && transaction.getItemDescription() == null))
                        editTextDescription.setBackgroundColor(getResources().getColor(R.color.no_color));
                    else
                        editTextDescription.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editTextDate.getText().toString().equals((String) DateFormat.format("dd.MM.yyyy", transaction.getDate().getTime())))
                        editTextDate.setBackgroundColor(getResources().getColor(R.color.no_color));
                    else
                        editTextDate.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextTransactionInterval.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editTextTransactionInterval.getText().toString().equals(Integer.toString(transaction.getTransactionInterval())))
                        editTextTransactionInterval.setBackgroundColor(getResources().getColor(R.color.no_color));
                    else
                        editTextTransactionInterval.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerTransactionType.getSelectedItem().toString().equals(transaction.getType().toString()))
                        spinnerTransactionType.setBackgroundColor(getResources().getColor(R.color.no_color));
                    else
                        spinnerTransactionType.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if(activity.equals("edit") && transaction.isDeleted()){
                enableTransactionEdit(false);
            }

        }

        return view;
    }

    private void saveChanges() {
        double budgetChange = 0;
        try {
            budgetChange = validate();
        } catch (Exception e) {
            return;
        }

        transaction.setTitle(editTextTitle.getText().toString());
        transaction.setType(TransactionType.getType(spinnerTransactionType.getSelectedItem().toString()));
        transaction.setAmount(Double.parseDouble(editTextAmount.getText().toString()));
        transaction.setItemDescription(editTextDescription.getText().toString());

        // Moze se izmjeniti
        String[] date = editTextDate.getText().toString().split("\\.", 3);
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1, Integer.parseInt(date[0]));
        transaction.setDate(cal.getTime());

        if(transaction.getType()==TransactionType.REGULARINCOME || transaction.getType()==TransactionType.REGULARPAYMENT){
            transaction.setTransactionInterval(Integer.parseInt(editTextTransactionInterval.getText().toString()));

            // Moze se izmjeniti
            date = editTextEndDateEdit.getText().toString().split("\\.", 3);
            cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1, Integer.parseInt(date[0]));
            transaction.setEndDate(cal.getTime());
        }
        else{
            transaction.setTransactionInterval(0);
            editTextTransactionInterval.setText("0");
            transaction.setEndDate(null);
            editTextEndDateEdit.setText("");
        }

        editTextTitle.setBackgroundColor(getResources().getColor(R.color.no_color));
        editTextAmount.setBackgroundColor(getResources().getColor(R.color.no_color));
        editTextDescription.setBackgroundColor(getResources().getColor(R.color.no_color));
        editTextDate.setBackgroundColor(getResources().getColor(R.color.no_color));
        editTextTransactionInterval.setBackgroundColor(getResources().getColor(R.color.no_color));
        spinnerTransactionType.setBackgroundColor(getResources().getColor(R.color.no_color));

        if(activity.equals("add")){
            getPresenter().addTransaction(transaction);
            finish();
        }
        else {
            getPresenter().editTransaction(transaction);
            if(((MainMVP.ActivityFuncions) getActivity()).isTwoPaneMode()) {
                ((MainMVP.ActivityFuncions) getActivity()).refreshList();
            }
        }


        getPresenter().updatedBudget(budgetChange);

        ((MainMVP.ActivityFuncions) getActivity()).refreshTransactions();
    }

    private double validate() {
        double amount;
        try {
            Transaction.validTitle(editTextTitle.getText().toString());
            DataChecker.validDate(editTextDate.getText().toString());
            int interval = Integer.parseInt(editTextTransactionInterval.getText().toString());
            if(spinnerTransactionType.getSelectedItem().toString().contains("REGULAR")) {
                DataChecker.validDate(editTextEndDateEdit.getText().toString());
                if(interval<1)
                    throw new IllegalArgumentException("Transaction interval must be grater than zero");
            }

            amount = Double.parseDouble(editTextAmount.getText().toString());
            if(amount < 0)
                throw new NumberFormatException("Amount must be positive");
            if(!(spinnerTransactionType.getSelectedItem().toString().equals(TransactionType.INDIVIDUALINCOME.toString()) ||
                    spinnerTransactionType.getSelectedItem().toString().equals(TransactionType.REGULARINCOME.toString()))){
                amount = -amount;
            }
            if(spinnerTransactionType.getSelectedItem().toString().contains("REGULAR")){
                amount *= DataChecker.getIntervalsBetween(DataChecker.getDateFromString(editTextDate.getText().toString()),
                        DataChecker.getDateFromString(editTextEndDateEdit.getText().toString()), interval);
            }

            if(transaction.getOrginalAmount()==0)
                amount -= transaction.getTotalAmount();
            else
                amount -= transaction.getOrginalAmount();


            double monthAmount = getPresenter().getMonthAmount(transaction.getDate());
            monthAmount += amount;
            double totalAmount = getPresenter().getTotalAmount();
            totalAmount += amount;

            if(monthAmount < -getPresenter().getAccount().getMonthLimit() &&
                    totalAmount < -getPresenter().getAccount().getTotalLimit()){
                alertDialog("Your transaction exceeds your total and monthly budget");
            }
            else if(monthAmount < -getPresenter().getAccount().getMonthLimit()){
                alertDialog("Your transaction exceeds your monthly budget");
            }
            else if(totalAmount < -getPresenter().getAccount().getTotalLimit()){
                alertDialog("Your transaction exceeds your total budget");
            }

        }
        // This catch is probably useless
        catch (NumberFormatException e){
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(R.string.incorrect_data);
            alertDialog.setMessage("You need to enter real number in amount and integer in transaction interval");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            throw new IllegalArgumentException();
        }catch (IllegalArgumentException e) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(R.string.incorrect_data);
            alertDialog.setMessage(e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            throw new IllegalArgumentException();
        }

        return amount;
    }

    private boolean alertDialog(String message){
        final boolean[] yes = {true};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setTitle(R.string.dialog_message_save);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                yes[0] = false;
            }
        });

        builder.create().show();

        return yes[0];
    }

    private void fillFieldsAgain(){
        activity = "add";

        ((TextView) view.findViewById(R.id.textViewEditOrAdd)).setText(R.string.add_transaction);
        transaction = new Transaction(new Date(), 0, "Transaction", TransactionType.INDIVIDUALINCOME,
                "");

        editTextTitle.setText(transaction.getTitle());
        editTextAmount.setText(getString(R.string.double_to_string, transaction.getAmount()));
        editTextDescription.setText(transaction.getItemDescription());
        editTextTransactionInterval.setText(getString(R.string.int_to_string, transaction.getTransactionInterval()));

        editTextDate.setText(DateFormat.format("dd.MM.yyyy", transaction.getDate().getTime()));

        if (!(transaction.getType().equals(TransactionType.REGULARPAYMENT) || transaction.getType().equals(TransactionType.REGULARINCOME)))
            editTextEndDateEdit.setText("");
        else {
            editTextEndDateEdit.setText(DateFormat.format("dd.MM.yyyy", transaction.getEndDate().getTime()));
        }

        buttonDelete.setEnabled(false);
    }

    private void finish(){
        if(((MainMVP.ActivityFuncions) getActivity()).isTwoPaneMode()){
            ((MainMVP.ActivityFuncions) getActivity()).refreshList();
            fillFieldsAgain();
        }
        else{
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


    public void removeOfflineMode(){
        textViewOfflineMode.setText("");
    }

    public void addOfflineMode(){
        if(transaction==null)
            textViewOfflineMode.setText(R.string.offline_adding);
        else
            textViewOfflineMode.setText(R.string.offline_editing);
    }

    private void enableTransactionEdit(boolean enable){
        buttonSave.setEnabled(enable);
        editTextTitle.setEnabled(enable);
        editTextAmount.setEnabled(enable);
        editTextDate.setEnabled(enable);
        editTextDescription.setEnabled(enable);
        editTextEndDateEdit.setEnabled(enable);
        editTextTransactionInterval.setEnabled(enable);
        spinnerTransactionType.setEnabled(enable);
        if(enable) {
            textViewOfflineMode.setText(R.string.offline_editing);
            buttonDelete.setText(R.string.delete);
        }
        else {
            textViewOfflineMode.setText(R.string.offline_deleting);
            buttonDelete.setText(R.string.undo);
        }
    }
}
