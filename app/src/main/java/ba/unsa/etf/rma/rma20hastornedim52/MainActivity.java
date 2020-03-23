package ba.unsa.etf.rma.rma20hastornedim52;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainMVP.View {

    private TransactionPresenter mPresenter;

    private TextView textViewGloablAmount;
    private TextView textViewLimit;
    private TextView textViewMonth;
    private Spinner spinnerFilter;
    private Spinner spinnerSortBy;
    private ListView listViewTransaction;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonAddTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new TransactionPresenter(this);

        textViewGloablAmount = (TextView) findViewById(R.id.textViewGloablAmount);
        textViewLimit = (TextView) findViewById(R.id.textViewLimit);
        textViewMonth = (TextView) findViewById(R.id.textViewMonth);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortBy);
        listViewTransaction = (ListView) findViewById(R.id.listViewTransaction);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonAddTransaction = (Button) findViewById(R.id.buttonAddTransaction);

        textViewGloablAmount.setText(getString(R.string.global_amount, mPresenter.getAccount().getBudget()));
        textViewLimit.setText(getString(R.string.limit, mPresenter.getAccount().getTotalLimit()));

        // Setting spinerSortBy
        ArrayAdapter<String> spinnerSortByAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[] {
                "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending", "Date - Ascending", "Date - Descending"
        });
        spinnerSortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(spinnerSortByAdapter);



    }
}
