package ba.unsa.etf.rma.rma20hastornedim52;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TransactionEditActivity extends AppCompatActivity {

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

        ( (TextView) findViewById(R.id.textViewEditOrAdd) ).setText(R.string.edit_transaction);
        ( (TextView) findViewById(R.id.textViewTitleEdit) ).setText(R.string.title);
        ( (TextView) findViewById(R.id.textViewAmountEdit) ).setText(R.string.amount);
        ( (TextView) findViewById(R.id.textViewTypeEdit) ).setText(R.string.type);
        ( (TextView) findViewById(R.id.textViewDescriptionEdit) ).setText(R.string.description);
        ( (TextView) findViewById(R.id.textViewDateEdit) ).setText(R.string.date);
        ( (TextView) findViewById(R.id.textViewTransactionIntervalEdit) ).setText(R.string.transaction_interval);
        ( (TextView) findViewById(R.id.textViewEndDateEdit) ).setText(R.string.end_date);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextTransactionInterval = (EditText) findViewById(R.id.editTextTransactionInterval);

        spinnerTransactionType = (Spinner) findViewById(R.id.spinnerTransactionType);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
    }
}
