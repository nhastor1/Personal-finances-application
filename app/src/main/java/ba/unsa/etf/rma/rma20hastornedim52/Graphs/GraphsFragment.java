package ba.unsa.etf.rma.rma20hastornedim52.Graphs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import ba.unsa.etf.rma.rma20hastornedim52.Budget.BudgetFragment;
import ba.unsa.etf.rma.rma20hastornedim52.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20hastornedim52.R;

public class GraphsFragment extends Fragment {

    private View view;
    private BarChart chart;

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

        return view;
    }

    private void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
