package ba.unsa.etf.rma.rma20hastornedim52.Adapter;

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

import ba.unsa.etf.rma.rma20hastornedim52.R;
import ba.unsa.etf.rma.rma20hastornedim52.Transaction;

public class TransactionListViewAdapter extends ArrayAdapter<Transaction> {

    private int resource;
    private Context context;

    public TransactionListViewAdapter(@NonNull Context context, int _resource, List<Transaction> items) {
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
            String inflater = Context.LAYOUT_INFLATER_SERVICE   ;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        Transaction transaction = getItem(position);

        if(transaction==null)
            return newView;

        TextView title = (TextView) newView.findViewById(R.id.textViewTransactionType);
        TextView amount = (TextView) newView.findViewById(R.id.textViewTransavtionAmount);
        ImageView icon = (ImageView) newView.findViewById(R.id.iconListViewTransaction);

        title.setText(transaction.getTitle());
        amount.setText(context.getResources().getString(R.string.double_to_string, transaction.getAmount()));

//            if(transaction.getType()==null)
//                return newView;

        switch(transaction.getType()){
            case INDIVIDUALINCOME:
                icon.setImageResource(R.drawable.individualincome);
                break;
            case REGULARINCOME:
                icon.setImageResource(R.drawable.regularincome);
                break;
            case INDIVIDUALPAYMENT:
                icon.setImageResource(R.drawable.individualpayment);
                break;
            case REGULARPAYMENT:
                icon.setImageResource(R.drawable.regularpayment);
                break;
            case PURCHASE:
                icon.setImageResource(R.drawable.purchase);
                break;

        }


        return newView;
    }

    public Transaction getTransaction(int position){
        return getItem(position);
    }

    public void setTransaction(List<Transaction> transactions){
        this.addAll(transactions);
    }


}
