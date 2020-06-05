package ba.unsa.etf.rma.rma20hastornedim52.TransactionActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Adapter.FilterSpinnerAdapter;
import ba.unsa.etf.rma.rma20hastornedim52.Adapter.TransactionListViewAdapter;
import ba.unsa.etf.rma.rma20hastornedim52.Budget.BudgetFragment;
import ba.unsa.etf.rma.rma20hastornedim52.Graphs.GraphsFragment;
import ba.unsa.etf.rma.rma20hastornedim52.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20hastornedim52.R;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;

public class TransactionListFragment extends Fragment implements MainMVP.View {

    View view;
    private OnItemClick oic;
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

    private int selectedItemPosition = -1;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction_list, container, false);

        textViewGloablAmount = (TextView) view.findViewById(R.id.textViewGloablAmount);
        textViewLimit = (TextView) view.findViewById(R.id.textViewLimit);
        textViewMonth = (TextView) view.findViewById(R.id.textViewMonth);
        spinnerFilter = (Spinner) view.findViewById(R.id.spinnerFilter);
        spinnerSortBy = (Spinner) view.findViewById(R.id.spinnerSortBy);
        listViewTransaction = (ListView) view.findViewById(R.id.listViewTransaction);
        buttonLeft = (Button) view.findViewById(R.id.buttonLeft);
        buttonRight = (Button) view.findViewById(R.id.buttonRight);
        buttonAddTransaction = (Button) view.findViewById(R.id.buttonAddTransaction);



        // Setting spinerSortBy
        ArrayAdapter<String> spinnerSortByAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[] {
                "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending", "Date - Ascending", "Date - Descending"
        });
        spinnerSortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(spinnerSortByAdapter);

        // Setting listViewTransaction
        transactionListViewAdapter = new TransactionListViewAdapter(getActivity().getApplicationContext(),
                R.layout.list_element_transaction, new ArrayList<Transaction>());
        listViewTransaction.setAdapter(transactionListViewAdapter);

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
        transactionSpinnerAdapter = new FilterSpinnerAdapter(getActivity().getApplicationContext(),
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

                getPresenter().refreshList();
                filterDate();
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

                getPresenter().refreshList();
                filterDate();
            }
        });

        // Setting filter on listViewTransaction
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().refreshList();
                filterDate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Setting sortBy on listViewTransaction
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().refreshList();
                String selectedItem = parent.getItemAtPosition(position).toString();
                sort(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterDate();

        buttonAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oic.onItemClicked(null);
            }
        });


        try {
            oic = (OnItemClick)getActivity();
        } catch (ClassCastException e) {

            throw new ClassCastException(getActivity().toString() +
                    "Treba implementirati OnItemClick");
        }

        listViewTransaction.setOnItemClickListener(listItemClickListener);

        if(!((MainMVP.ActivityFuncions) getActivity()).isTwoPaneMode()) {
            view.setOnTouchListener(onSwipeTouchListener);
            listViewTransaction.setOnTouchListener(onSwipeTouchListener);
        }

        return view;
    }

    private View.OnTouchListener onSwipeTouchListener =  new OnSwipeTouchListener(getContext()) {
        @Override
        public void onSwipeLeft() {
            Fragment fragment = new BudgetFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list_frame, fragment).addToBackStack(null)
                    .commit();
        }

        @Override
        public void onSwipeRight() {
            Fragment fragment = new GraphsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list_frame, fragment).addToBackStack(null)
                    .commit();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        filterDate();
        refresh();
    }

    @Override
    public TransactionPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TransactionPresenter(this,getActivity().getApplicationContext());
        }
        return mPresenter;
    }

    @Override
    public void setTransactions(List<Transaction> transactions){
        //transactionListViewAdapter.clear();
        transactionListViewAdapter.setTransaction(transactions);
    }

    @Override
    public void notifyTransactionListDataSetChanged(){
        transactionListViewAdapter.notifyDataSetChanged();
        try {
            textViewGloablAmount.setText(getString(R.string.global_amount, getPresenter().getGloabalAmount()));
        }catch (IllegalStateException e){
            // If fragment is not attaached do nothing
        }
    }

    @Override
    public int getCurrentMonth(){
        String[] monthAndYear = textViewMonth.getText().toString().split(", ");
        return getPresenter().getMonthFromName(monthAndYear[0]);
    }

    @Override
    public int getCurrentYear(){
        String[] monthAndYear = textViewMonth.getText().toString().split(", ");
        return Integer.parseInt(monthAndYear[1]);
    }

    @Override
    public void refresh(){
        try {
            textViewGloablAmount.setText(getString(R.string.global_amount, getPresenter().getGloabalAmount()));
            textViewLimit.setText(getString(R.string.limit, getPresenter().getAccount().getTotalLimit()));
        }catch (IllegalStateException e){
            // If fragment is not attached do nothing
        }

        getPresenter().refreshTransactions();
    }

    private void setDate(){
        textViewMonth.setText(getString(R.string.month_year, getPresenter().getMonthName(date), getPresenter().getYear(date)));
    }

    private void sort(String selectedItem){
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
        notifyTransactionListDataSetChanged();
    }

    private void filter(String selectedItem){
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
        sort(spinnerSortBy.getSelectedItem().toString());
    }

    @Override
    public void filterDate(){
        filter(spinnerFilter.getSelectedItem().toString());

        String[] monthAndYear = textViewMonth.getText().toString().split(", ");
        transactionListViewAdapter = getPresenter().filterDate(monthAndYear[0], Integer.parseInt(monthAndYear[1]));
        listViewTransaction.setAdapter(transactionListViewAdapter);
        notifyTransactionListDataSetChanged();
    }

    public interface OnItemClick {
        void onItemClicked(Transaction transaction);
    }

    private AdapterView.OnItemClickListener listItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    if(!((MainMVP.ActivityFuncions) getActivity()).isTwoPaneMode()){
                        Transaction transaction = transactionListViewAdapter.getTransaction(position);
                        oic.onItemClicked(transaction);
                        return;
                    }
                    if(selectedItemPosition == position){
                        listViewTransaction.getChildAt(position).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                        selectedItemPosition = -1;
                        buttonAddTransaction.callOnClick();
                    }
                    else{
                        if(selectedItemPosition!=-1)
                            listViewTransaction.getChildAt(selectedItemPosition).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                        selectedItemPosition = position;
                        listViewTransaction.getChildAt(position).setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                        Transaction transaction = transactionListViewAdapter.getTransaction(position);
                        oic.onItemClicked(transaction);
                    }
                }
            };
}
