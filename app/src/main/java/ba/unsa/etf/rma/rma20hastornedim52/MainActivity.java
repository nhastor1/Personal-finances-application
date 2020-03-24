package ba.unsa.etf.rma.rma20hastornedim52;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainMVP.View {

    private TransactionPresenter mPresenter;
    private TransactionListViewAdapter transactionListViewAdapter;
    private FilterSpinnerAdapter transactionSpinnerAdapter;
    private Date date = new Date();

    private TextView textViewGloablAmount;
    private TextView textViewLimit;
    private TextView textViewMonth;
    private Spinner spinnerFilter;
    private Spinner spinnerSortBy;
    private ListView listViewTransaction;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonAddTransaction;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewGloablAmount = (TextView) findViewById(R.id.textViewGloablAmount);
        textViewLimit = (TextView) findViewById(R.id.textViewLimit);
        textViewMonth = (TextView) findViewById(R.id.textViewMonth);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortBy);
        listViewTransaction = (ListView) findViewById(R.id.listViewTransaction);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonAddTransaction = (Button) findViewById(R.id.buttonAddTransaction);

        textViewGloablAmount.setText(getString(R.string.global_amount, getPresenter().getAccount().getBudget()));
        textViewLimit.setText(getString(R.string.limit, getPresenter().getAccount().getTotalLimit()));

        // Setting spinerSortBy
        ArrayAdapter<String> spinnerSortByAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[] {
                "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending", "Date - Ascending", "Date - Descending"
        });
        spinnerSortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(spinnerSortByAdapter);

        // Setting listViewTransaction
        transactionListViewAdapter = new TransactionListViewAdapter(getApplicationContext(),
                R.layout.list_element_transaction, new ArrayList<Transaction>());
        listViewTransaction.setAdapter(transactionListViewAdapter);
        getPresenter().refreshTransactions();

        // Setting spinnerFilter
        List<String> types = new ArrayList<String>(){
            {
                add("ALL");
                add("INDIVIDUALPAYMENT");
                add("REGULARPAYMENT");
                add("INDIVIDUALINCOME");
                add("REGULARINCOME");
                add("PURCHASE");
            }
        };
        transactionSpinnerAdapter = new FilterSpinnerAdapter(getApplicationContext(),
                R.layout.spinner_filter_element, types);
        transactionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(transactionSpinnerAdapter);


        // Setting textViewMonth
        setDate();

        // Setting buttonRight
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date.getTime());
                cal.add(Calendar.MONTH, 1);
                date = cal.getTime();
                setDate();
            }
        });

        // Setting buttonLeft
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date.getTime());
                cal.add(Calendar.MONTH, -1);
                date = cal.getTime();
                setDate();
            }
        });

        // Setting filter on listViewTransaction
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "INDIVIDUALINCOME":
                        transactionListViewAdapter = getPresenter().filterIndividualincome();
                        break;
                    case "REGULARINCOME":
                        transactionListViewAdapter = getPresenter().filterRegularincome();
                        break;
                    case "INDIVIDUALPAYMENT":
                        transactionListViewAdapter = getPresenter().filterIndividualpayment();
                        break;
                    case "REGULARPAYMENT":
                        transactionListViewAdapter = getPresenter().filterRegularpayment();
                        break;
                    case "PURCHASE":
                        transactionListViewAdapter = getPresenter().filterPurchase();
                        break;
                    default:
                        transactionListViewAdapter = getPresenter().filterAll();
                }
                listViewTransaction.setAdapter(transactionListViewAdapter);
                notifyMovieListDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Setting sortBy on listViewTransaction
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "Price - Ascending":
                        transactionListViewAdapter = getPresenter().sortPriceASC();
                        break;
                    case "Price - Descending":
                        transactionListViewAdapter = getPresenter().sortPriceDSC();
                        break;
                    case "Title - Ascending":
                        transactionListViewAdapter = getPresenter().sortTitleASC();
                        break;
                    case "Title - Descending":
                        transactionListViewAdapter = getPresenter().sortTitleDSC();
                        break;
                    case "Date - Ascending":
                        transactionListViewAdapter = getPresenter().sortDateASC();
                        break;
                    case "Date - Descending":
                        transactionListViewAdapter = getPresenter().sortDateDSC();
                }
                listViewTransaction.setAdapter(transactionListViewAdapter);
                notifyMovieListDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public TransactionPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TransactionPresenter(this,this);
        }
        return mPresenter;
    }

    @Override
    public void setTransactions(List<Transaction> transactions){
        transactionListViewAdapter.setTransaction(transactions);
    }

    @Override
    public void notifyMovieListDataSetChanged(){
        transactionListViewAdapter.notifyDataSetChanged();
    }

    private void setDate(){
        textViewMonth.setText(getString(R.string.month_year, getPresenter().getMonth(date), getPresenter().getYear(date)));
    }
}
