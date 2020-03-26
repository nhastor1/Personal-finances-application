package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionEditActivity extends AppCompatActivity {

    private int id;
    private Transaction transaction;

    private TextView textViewEndDateEdit2;

    private EditText editTextTitle;
    private EditText editTextAmount;
    private EditText editTextDescription;
    private EditText editTextDate;
    private EditText editTextTransactionInterval;

    private Spinner spinnerTransactionType;

    private Button buttonSave;
    private Button buttonDelete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_transaction);

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        transaction = getTransaction(id);

        ( (TextView) findViewById(R.id.textViewEditOrAdd) ).setText(R.string.edit_transaction);
        ( (TextView) findViewById(R.id.textViewTitleEdit) ).setText(R.string.title);
        ( (TextView) findViewById(R.id.textViewAmountEdit) ).setText(R.string.amount);
        ( (TextView) findViewById(R.id.textViewTypeEdit) ).setText(R.string.type);
        ( (TextView) findViewById(R.id.textViewDescriptionEdit) ).setText(R.string.description);
        ( (TextView) findViewById(R.id.textViewDateEdit) ).setText(R.string.date);
        ( (TextView) findViewById(R.id.textViewTransactionIntervalEdit) ).setText(R.string.transaction_interval);
        ( (TextView) findViewById(R.id.textViewEndDateEdit) ).setText(R.string.end_date);

        textViewEndDateEdit2 = (TextView) findViewById(R.id.textViewEndDateEdit2);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextTransactionInterval = (EditText) findViewById(R.id.editTextTransactionInterval);

        editTextTitle.setText(transaction.getTitle());
        editTextAmount.setText(getString(R.string.double_to_string, transaction.getAmount()));
        editTextDescription.setText(transaction.getItemDescription());
        editTextTransactionInterval.setText(getString(R.string.int_to_string, transaction.getTransactionInterval()));

        editTextDate.setText(DateFormat.format("dd.MM.yyyy", transaction.getDate().getTime()));

        if(!(transaction.getType().equals(TransactionType.REGULARPAYMENT) || transaction.getType().equals(TransactionType.REGULARINCOME)))
            textViewEndDateEdit2.setText(getString(R.string.no_date));
        else{
            textViewEndDateEdit2.setText(DateFormat.format("dd.MM.yyyy", transaction.getEndDate().getTime()));
        }

        spinnerTransactionType = (Spinner) findViewById(R.id.spinnerTransactionType);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonSave.setText(R.string.save);
        buttonDelete.setText(R.string.delete);

        // Setting buttonDelete
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TransactionModel.transactions.remove(transaction);

                        finish();
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

        // Setting buttonSave
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveChanges();

//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
//                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        TransactionModel.transactions.set(id, transaction);
//                    }
//                });
//                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//
//                builder.create().show();
            }
        });

        // Setting spinnerFilter
        List<String> types = new ArrayList<String>(){
            {
                add("INDIVIDUALPAYMENT");
                add("REGULARPAYMENT");
                add("INDIVIDUALINCOME");
                add("REGULARINCOME");
                add("PURCHASE");
            }
        };
        FilterSpinnerAdapter transactionTypeAdapter = new FilterSpinnerAdapter(getApplicationContext(),
                R.layout.spinner_filter_element, types);
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransactionType.setAdapter(transactionTypeAdapter);

        switch (transaction.getType()) {
            case INDIVIDUALINCOME:
                spinnerTransactionType.setSelection(2);
                break;
            case REGULARINCOME:
                spinnerTransactionType.setSelection(3);
                break;
            case REGULARPAYMENT:
                spinnerTransactionType.setSelection(1);
                break;
            default:
                spinnerTransactionType.setSelection(4);
                break;
        }


        // On text change listeners

        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextTitle.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
//                editTextTitle.getBackground().setColorFilter(R.color.colorAccent);
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
                editTextTransactionInterval.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTransactionType.setBackgroundColor(getResources().getColor(R.color.colorChangeData));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void saveChanges() {
        try {
            validate();
        } catch (Exception e) {
            return;
        }

        transaction.setTitle(editTextTitle.getText().toString());
        transaction.setType(TransactionType.getType(spinnerTransactionType.getSelectedItem().toString()));
        transaction.setAmount(Double.parseDouble(editTextAmount.getText().toString()));
        transaction.setItemDescription(editTextDescription.getText().toString());

        String[] date = editTextDate.getText().toString().split("\\.", 3);
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1, Integer.parseInt(date[0]));
        transaction.setDate(cal.getTime());

        if(transaction.getType()==TransactionType.REGULARINCOME || transaction.getType()==TransactionType.REGULARPAYMENT){
            transaction.setTransactionInterval(Integer.parseInt(editTextTransactionInterval.getText().toString()));
            cal.add(Calendar.DAY_OF_MONTH, transaction.getTransactionInterval());
            transaction.setEndDate(cal.getTime());
            textViewEndDateEdit2.setText(DateFormat.format("dd.MM.yyyy", transaction.getEndDate().getTime()));
        }
        else{
            transaction.setTransactionInterval(0);
            editTextTransactionInterval.setText("0");
            transaction.setEndDate(null);
            textViewEndDateEdit2.setText(R.string.no_date);
        }
        editTextTitle.setBackgroundColor(getResources().getColor(R.color.no_color));
        editTextAmount.setBackgroundColor(getResources().getColor(R.color.no_color));
        editTextDescription.setBackgroundColor(getResources().getColor(R.color.no_color));
        editTextDate.setBackgroundColor(getResources().getColor(R.color.no_color));
        editTextTransactionInterval.setBackgroundColor(getResources().getColor(R.color.no_color));
        spinnerTransactionType.setBackgroundColor(getResources().getColor(R.color.no_color));
    }

    private void validate() {
        try {
            Transaction.validTitle(editTextTitle.getText().toString());
            Transaction.validDate(editTextDate.getText().toString());
        } catch (Exception e) {
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
    }

    private Transaction getTransaction(int id){
        return TransactionModel.transactions.get(id);
    }

    private AppCompatActivity getActivity(){
        return this;
    }
}
