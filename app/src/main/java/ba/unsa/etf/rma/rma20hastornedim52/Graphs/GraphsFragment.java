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

import ba.unsa.etf.rma.rma20hastornedim52.Adapter.FilterSpinnerAdapter;
import ba.unsa.etf.rma.rma20hastornedim52.Budget.BudgetFragment;
import ba.unsa.etf.rma.rma20hastornedim52.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20hastornedim52.R;

public class GraphsFragment extends Fragment {

    private View view;

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

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
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
        });

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
        //spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(android.R.color.white));
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        chartPayment = (BarChart) view.findViewById(R.id.chartPayment);
        chartIncome = (BarChart) view.findViewById(R.id.chartIncome);
        chartAll = (BarChart) view.findViewById(R.id.chartAll);

        chartPayment.getDescription().setEnabled(false);
        chartIncome.getDescription().setEnabled(false);
        chartAll.getDescription().setEnabled(false);

        chartPayment.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        chartIncome.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        chartAll.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));

        BarDataSet barDataSet = new BarDataSet(dataValues1(), "chartAll");

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        chartPayment.setData(barData);
        chartPayment.invalidate();

        chartIncome.setData(barData);
        chartIncome.invalidate();

        chartAll.setData(barData);
        chartAll.invalidate();

        return view;
    }

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
}
