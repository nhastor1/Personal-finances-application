package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FilterSpinnerAdapter extends ArrayAdapter<String> {

    private int resource;
    private Context context;

    public FilterSpinnerAdapter(@NonNull Context context, int _resource, @NonNull List<String> items) {
        super(context, _resource, items);
        resource = _resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout newView;
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        String transactionType = getItem(position);

        if(transactionType==null)
            return newView;

        TextView title = (TextView) newView.findViewById(R.id.textViewTransactionType);
        ImageView icon = (ImageView) newView.findViewById(R.id.iconSpinnerFilter);

        switch(transactionType){
            case "INDIVIDUALINCOME":
                icon.setImageResource(R.drawable.individualincome);
                title.setText(R.string.individualincome);
                break;
            case "REGULARINCOME":
                icon.setImageResource(R.drawable.regularincome);
                title.setText(R.string.regularincome);
                break;
            case "INDIVIDUALPAYMENT":
                icon.setImageResource(R.drawable.individualpayment);
                title.setText(R.string.individualpayment);
                break;
            case "REGULARPAYMENT":
                icon.setImageResource(R.drawable.regularpayment);
                title.setText(R.string.regularpayment);
                break;
            case "PURCHASE":
                icon.setImageResource(R.drawable.purchase);
                title.setText(R.string.purchase);
                break;
            default:
                icon.setImageResource(R.drawable.all);
                title.setText(R.string.all);
        }


        return newView;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout newView;
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        String transactionType = getItem(position);

        if(transactionType==null)
            return newView;

        TextView title = (TextView) newView.findViewById(R.id.textViewTransactionType);
        ImageView icon = (ImageView) newView.findViewById(R.id.iconSpinnerFilter);

        switch(transactionType){
            case "INDIVIDUALINCOME":
                icon.setImageResource(R.drawable.individualincome);
                title.setText(R.string.individualincome);
                break;
            case "REGULARINCOME":
                icon.setImageResource(R.drawable.regularincome);
                title.setText(R.string.regularincome);
                break;
            case "INDIVIDUALPAYMENT":
                icon.setImageResource(R.drawable.individualpayment);
                title.setText(R.string.individualpayment);
                break;
            case "REGULARPAYMENT":
                icon.setImageResource(R.drawable.regularpayment);
                title.setText(R.string.regularpayment);
                break;
            case "PURCHASE":
                icon.setImageResource(R.drawable.purchase);
                title.setText(R.string.purchase);
                break;
            default:
                icon.setImageResource(R.drawable.all);
                title.setText(R.string.all);
        }


        return newView;
    }

    public String getTransactionType(int position){
        return getItem(position);
    }

    public void setTransaction(List<String> transactionTypes){
        this.addAll(transactionTypes);
    }
}
