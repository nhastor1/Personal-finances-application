package ba.unsa.etf.rma.rma20hastornedim52.Graphs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Budget.BudgetFragment;
import ba.unsa.etf.rma.rma20hastornedim52.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20hastornedim52.R;

public class GraphsFragment extends Fragment implements GraphsMVP.View{

    private View view;
    private GraphsMVP.Presenter presenter;

    private Spinner spinner;
    private ArrayAdapter<String> spinerAdapter;

    private BarChart chartPayment;
    private BarChart chartIncome;
    private BarChart chartAll;

    private AdapterView.OnItemSelectedListener SpinnerOnItemSelectedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graphs, container, false);

        view.setOnTouchListener(onSwipeListener);

        // Setting spinnerChartInterval
        spinner = (Spinner) view.findViewById(R.id.spinnerChartInterval);

        List<String> chartInterval = new ArrayList<String>(){
            {
                add("Days");
                add("Weeks");
                add("Months");
            }
        };
        spinerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, chartInterval);
        spinner.setAdapter(spinerAdapter);
        spinner.setSelection(2);

        chartPayment = (BarChart) view.findViewById(R.id.chartPayment);
        chartIncome = (BarChart) view.findViewById(R.id.chartIncome);
        chartAll = (BarChart) view.findViewById(R.id.chartAll);

        chartPayment.setOnTouchListener(onSwipeListener);
        chartIncome.setOnTouchListener(onSwipeListener);
        chartAll.setOnTouchListener(onSwipeListener);

        chartPayment.getDescription().setEnabled(false);
        chartIncome.getDescription().setEnabled(false);
        chartAll.getDescription().setEnabled(false);

        chartPayment.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        chartIncome.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        chartAll.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));

        spinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);

        return view;
    }

    @Override
    public GraphsMVP.Presenter getPresenter() {
        if(presenter==null)
            presenter = new GraphsPresenter(this, getContext());
        return presenter;
    }

    private AdapterView.OnItemSelectedListener spinnerOnItemSelectedListener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(android.R.color.white));
            if(position == 0){
                setChartPaymentDays();
                setChartIncomeDays();
                setChartAllDays();
            }
            else if(position==1){
                setChartPaymentWeeks();
                setChartIncomeWeeks();
                setChartAllWeeks();
            }
            else{
                setChartPaymentMonths();
                setChartIncomeMonths();
                setChartAllMonths();
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private OnSwipeTouchListener onSwipeListener = new OnSwipeTouchListener(getContext()) {
        @Override
        public void onSwipeRight() {
            finish();
            Fragment fragment = new BudgetFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_list_frame, fragment).addToBackStack(null)
                    .commit();
        }

        @Override
        public void onSwipeLeft() {
            finish();
        }
    };

    private ArrayList<BarEntry> dataValues1(){
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        dataValues.add(new BarEntry(0, 1));
        dataValues.add(new BarEntry(1, 2));
        dataValues.add(new BarEntry(2, 5));
        dataValues.add(new BarEntry(3, 1));
        dataValues.add(new BarEntry(5, 7));
        return dataValues;
    }

    private void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void setChartPaymentDays(){
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getDayPayments();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) -days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "Payments by days");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartPayment.setData(barData);
        chartPayment.invalidate();
    }

    private void setChartIncomeDays() {
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getDayIncome();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "Income by days");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartIncome.setData(barData);
        chartIncome.invalidate();
    }

    private void setChartAllDays() {
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getDayAll();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "All transactions by days");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartAll.setData(barData);
        chartAll.invalidate();
    }

    private void setChartPaymentWeeks() {
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getWeeksPayments();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) -days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "Payments by weeks of month");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartPayment.setData(barData);
        chartPayment.invalidate();
    }

    private void setChartIncomeWeeks() {
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getWeeksIncome();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "Income by weeks of month");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartIncome.setData(barData);
        chartIncome.invalidate();
    }

    private void setChartAllWeeks() {
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getWeeksAll();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "All transactions by weeks of month");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartAll.setData(barData);
        chartAll.invalidate();
    }

    private void setChartPaymentMonths() {
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getMonthsPayments();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) -days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "Payments by months");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartPayment.setData(barData);
        chartPayment.invalidate();
    }

    private void setChartIncomeMonths() {
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getMonthsIncome();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "Income by months");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartIncome.setData(barData);
        chartIncome.invalidate();
    }

    private void setChartAllMonths() {
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        double days[] = getPresenter().getMonthsAll();
        for(int i=0; i<days.length; i++)
            dataValues.add(new BarEntry(i+1, (float) days[i]));
        BarDataSet barDataSet = new BarDataSet(dataValues, "All transactions by months");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        chartAll.setData(barData);
        chartAll.invalidate();
    }

}
