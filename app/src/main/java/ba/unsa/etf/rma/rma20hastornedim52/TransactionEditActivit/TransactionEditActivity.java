//package ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.text.format.DateFormat;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import ba.unsa.etf.rma.rma20hastornedim52.DataChecker;
//import ba.unsa.etf.rma.rma20hastornedim52.Adapter.FilterSpinnerAdapter;
//import ba.unsa.etf.rma.rma20hastornedim52.R;
//import ba.unsa.etf.rma.rma20hastornedim52.Transaction;
//import ba.unsa.etf.rma.rma20hastornedim52.TransactionType;
//
//public class TransactionEditActivity extends AppCompatActivity implements TransactionEditMVP.View {
//
//    private TransactionEditMVP.Presenter presenter;
//
//    private int id;
//    private Transaction transaction;
//
//    private TextView textViewEndDateEdit2;
//
//    private EditText editTextTitle;
//    private EditText editTextAmount;
//    private EditText editTextDescription;
//    private EditText editTextDate;
//    private EditText editTextTransactionInterval;
//
//    private Spinner spinnerTransactionType;
//
//    private Button buttonSave;
//    private Button buttonDelete;
//
//    private String activity;
//
//    public TransactionEditMVP.Presenter getPresenter() {
//        if (presenter == null) {
//            presenter = new TransactionEditPresenter(this,this, transaction);
//        }
//        return presenter;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.edit_transaction);
//
//        id = Integer.parseInt(getIntent().getStringExtra("id"));
//        activity = getIntent().getStringExtra("type_of_action");
//
//
//        if(activity.equals("edit")) {
//            ((TextView) findViewById(R.id.textViewEditOrAdd)).setText(R.string.edit_transaction);
//            transaction = getPresenter().getTransaction(id);
//        }
//        else{
//            ( (TextView) findViewById(R.id.textViewEditOrAdd) ).setText(R.string.add_transaction);
//            transaction = new Transaction(new Date(), 0, "Transaction", TransactionType.INDIVIDUALINCOME,
//                    "", 0);
//        }
//        ( (TextView) findViewById(R.id.textViewTitleEdit) ).setText(R.string.title);
//        ( (TextView) findViewById(R.id.textViewAmountEdit) ).setText(R.string.amount);
//        ( (TextView) findViewById(R.id.textViewTypeEdit) ).setText(R.string.type);
//        ( (TextView) findViewById(R.id.textViewDescriptionEdit) ).setText(R.string.description);
//        ( (TextView) findViewById(R.id.textViewDateEdit) ).setText(R.string.date);
//        ( (TextView) findViewById(R.id.textViewTransactionIntervalEdit) ).setText(R.string.transaction_interval);
//        ( (TextView) findViewById(R.id.textViewEndDateEdit) ).setText(R.string.end_date);
//
//        textViewEndDateEdit2 = (TextView) findViewById(R.id.textViewEndDateEdit2);
//
//        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
//        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
//        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
//        editTextDate = (EditText) findViewById(R.id.editTextDate);
//        editTextTransactionInterval = (EditText) findViewById(R.id.editTextTransactionInterval);
//
//        editTextTitle.setText(transaction.getTitle());
//        editTextAmount.setText(getString(R.string.double_to_string, transaction.getAmount()));
//        editTextDescription.setText(transaction.getItemDescription());
//        editTextTransactionInterval.setText(getString(R.string.int_to_string, transaction.getTransactionInterval()));
//
//        editTextDate.setText(DateFormat.format("dd.MM.yyyy", transaction.getDate().getTime()));
//
//        if(!(transaction.getType().equals(TransactionType.REGULARPAYMENT) || transaction.getType().equals(TransactionType.REGULARINCOME)))
//            textViewEndDateEdit2.setText(getString(R.string.no_date));
//        else{
//            textViewEndDateEdit2.setText(DateFormat.format("dd.MM.yyyy", transaction.getEndDate().getTime()));
//        }
//
//        spinnerTransactionType = (Spinner) findViewById(R.id.spinnerTransactionType);
//
//        buttonSave = (Button) findViewById(R.id.buttonSave);
//        buttonDelete = (Button) findViewById(R.id.buttonDelete);
//
//        buttonSave.setText(R.string.save);
//        buttonDelete.setText(R.string.delete);
//
//        if(activity.equals("edit")) {
//            // Setting buttonDelete
//            buttonDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setMessage(R.string.dialog_message_delete).setTitle(R.string.dialog_title);
//
//                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            getPresenter().removeTransaction(transaction);
//
//                            finish();
//                        }
//                    });
//                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User cancelled the dialog
//                        }
//                    });
//
//                    builder.create().show();
//                }
//            });
//        }else {
//            buttonDelete.setEnabled(false);
//        }
//
//        // Setting buttonSave
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    saveChanges();
//            }
//        });
//
//        // Setting spinnerFilter
//        List<String> types = new ArrayList<String>(){
//            {
//                add("INDIVIDUALPAYMENT");
//                add("REGULARPAYMENT");
//                add("INDIVIDUALINCOME");
//                add("REGULARINCOME");
//                add("PURCHASE");
//            }
//        };
//        FilterSpinnerAdapter transactionTypeAdapter = new FilterSpinnerAdapter(getApplicationContext(),
//                R.layout.spinner_filter_element, types);
//        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTransactionType.setAdapter(transactionTypeAdapter);
//
//        switch (transaction.getType()) {
//            case INDIVIDUALPAYMENT:
//                spinnerTransactionType.setSelection(0);
//                break;
//            case REGULARPAYMENT:
//                spinnerTransactionType.setSelection(1);
//                break;
//            case INDIVIDUALINCOME:
//                spinnerTransactionType.setSelection(2);
//                break;
//            case REGULARINCOME:
//                spinnerTransactionType.setSelection(3);
//                break;
//            default:
//                spinnerTransactionType.setSelection(4);
//        }
//
//
//        // On text change listeners
//
//        editTextTitle.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(editTextTitle.getText().toString().equals(transaction.getTitle()))
//                    editTextTitle.setBackgroundColor(getResources().getColor(R.color.no_color));
//                else
//                    editTextTitle.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        editTextAmount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(editTextAmount.getText().toString().equals(getString(R.string.double_to_string, transaction.getAmount())))
//                    editTextAmount.setBackgroundColor(getResources().getColor(R.color.no_color));
//                else
//                    editTextAmount.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        editTextDescription.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(editTextDescription.getText().toString().equals(transaction.getItemDescription()) ||
//                        (editTextDescription.getText().toString().equals("") && transaction.getItemDescription()==null))
//                    editTextDescription.setBackgroundColor(getResources().getColor(R.color.no_color));
//                else
//                    editTextDescription.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        editTextDate.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(editTextDate.getText().toString().equals((String) DateFormat.format("dd.MM.yyyy", transaction.getDate().getTime())))
//                    editTextDate.setBackgroundColor(getResources().getColor(R.color.no_color));
//                else
//                    editTextDate.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        editTextTransactionInterval.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(editTextTransactionInterval.getText().toString().equals(Integer.toString(transaction.getTransactionInterval())))
//                    editTextTransactionInterval.setBackgroundColor(getResources().getColor(R.color.no_color));
//                else
//                    editTextTransactionInterval.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(spinnerTransactionType.getSelectedItem().toString().equals(transaction.getType().toString()))
//                    spinnerTransactionType.setBackgroundColor(getResources().getColor(R.color.no_color));
//                else
//                    spinnerTransactionType.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    private void saveChanges() {
//        try {
//            validate();
//        } catch (Exception e) {
//            return;
//        }
//
//        transaction.setTitle(editTextTitle.getText().toString());
//        transaction.setType(TransactionType.getType(spinnerTransactionType.getSelectedItem().toString()));
//        transaction.setAmount(Double.parseDouble(editTextAmount.getText().toString()));
//        transaction.setItemDescription(editTextDescription.getText().toString());
//
//        String[] date = editTextDate.getText().toString().split("\\.", 3);
//        Calendar cal = Calendar.getInstance();
//        cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1, Integer.parseInt(date[0]));
//        transaction.setDate(cal.getTime());
//
//        if(transaction.getType()==TransactionType.REGULARINCOME || transaction.getType()==TransactionType.REGULARPAYMENT){
//            transaction.setTransactionInterval(Integer.parseInt(editTextTransactionInterval.getText().toString()));
//            cal.add(Calendar.DAY_OF_MONTH, transaction.getTransactionInterval());
//            transaction.setEndDate(cal.getTime());
//            textViewEndDateEdit2.setText(DateFormat.format("dd.MM.yyyy", transaction.getEndDate().getTime()));
//        }
//        else{
//            transaction.setTransactionInterval(0);
//            editTextTransactionInterval.setText("0");
//            transaction.setEndDate(null);
//            textViewEndDateEdit2.setText(R.string.no_date);
//        }
//        editTextTitle.setBackgroundColor(getResources().getColor(R.color.no_color));
//        editTextAmount.setBackgroundColor(getResources().getColor(R.color.no_color));
//        editTextDescription.setBackgroundColor(getResources().getColor(R.color.no_color));
//        editTextDate.setBackgroundColor(getResources().getColor(R.color.no_color));
//        editTextTransactionInterval.setBackgroundColor(getResources().getColor(R.color.no_color));
//        spinnerTransactionType.setBackgroundColor(getResources().getColor(R.color.no_color));
//
//        if(activity.equals("add")){
//            getPresenter().addTransaction(transaction);
//            finish();
//        }
//    }
//
//    private void validate() {
//        try {
//            Transaction.validTitle(editTextTitle.getText().toString());
//            DataChecker.validDate(editTextDate.getText().toString());
//            Integer.parseInt(editTextTransactionInterval.getText().toString());
//            double amount = Double.parseDouble(editTextAmount.getText().toString());
//            if(spinnerTransactionType.getSelectedItem().toString().equals(TransactionType.INDIVIDUALINCOME.toString()) ||
//                    spinnerTransactionType.getSelectedItem().toString().equals(TransactionType.REGULARINCOME.toString())){
//                if(amount<0)
//                    amount = -amount;
//            }
//            else if(amount>0)
//                amount = -amount;
//
//            amount -= transaction.getAmount();
//
//            double monthAmount = getPresenter().getMonthAmount(transaction.getDate());
//            monthAmount += amount;
//            double totalAmount = getPresenter().getTotalAmount();
//            totalAmount += amount;
//
//            if(monthAmount < -getPresenter().getAccount().getMonthLimit() &&
//                totalAmount < -getPresenter().getAccount().getTotalLimit()){
//                alertDialog("Your transaction exceeds your total and monthly budget");
//            }
//            else if(monthAmount < -getPresenter().getAccount().getMonthLimit()){
//                alertDialog("Your transaction exceeds your monthly budget");
//            }
//            else if(totalAmount < -getPresenter().getAccount().getTotalLimit()){
//                alertDialog("Your transaction exceeds your total budget");
//            }
//
//        }
//        // This catch is probably useless
//        catch (NumberFormatException e){
//            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//            alertDialog.setTitle(R.string.incorrect_data);
//            alertDialog.setMessage("You need to enter real number in amount and integer in transaction interval");
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
//            throw new IllegalArgumentException();
//        }catch (IllegalArgumentException e) {
//            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//            alertDialog.setTitle(R.string.incorrect_data);
//            alertDialog.setMessage(e.getMessage());
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
//            throw new IllegalArgumentException();
//        }
//    }
//
//    private AppCompatActivity getActivity(){
//        return this;
//    }
//
//    private boolean alertDialog(String message){
//        final boolean[] yes = {true};
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage(message).setTitle(R.string.dialog_message_save);
//
//        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                //
//            }
//        });
//        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                yes[0] = false;
//            }
//        });
//
//        builder.create().show();
//
//        return yes[0];
//    }
//}
