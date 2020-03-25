package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TransactionEditActivity extends AppCompatActivity {

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

        transaction = getTransaction(getIntent().getStringExtra("title"));

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

        CharSequence s  = DateFormat.format("dd.MM.yyyy", transaction.getDate().getTime());
        editTextDate.setText(s);

        if(!(transaction.getType().equals(TransactionType.REGULARPAYMENT) || transaction.getType().equals(TransactionType.REGULARINCOME)))
            textViewEndDateEdit2.setText(getString(R.string.no_date));

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


    }

    private Transaction getTransaction(String title){
        for(Transaction t : TransactionModel.transactions)
            if(t.getTitle().equals(title))
                return t;

        return null;
    }

    private AppCompatActivity getActivity(){
        return this;
    }
}
