package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.rma20hastornedim52.Budget.BudgetInteractor;
import ba.unsa.etf.rma.rma20hastornedim52.TransactionEditActivit.TransactionEditInteractor;

public class UpdateService  implements TransactionEditInteractor.OnTransactionAddDone,
        TransactionEditInteractor.OnTransactionEditDone, TransactionEditInteractor.OnTransactionRemoveDone,
        BudgetInteractor.OnAccountSearchDone {

    private Context context;
    private boolean getModel;
    private List<Transaction> transactions = new ArrayList<>();
    private List<Integer> typeOfChangeList = new ArrayList<>();
    private Account account;

    public UpdateService(Context context, boolean getModel) {
        this.context = context;
        this.getModel = getModel;
        getTransactionFromDatabase();
        if(!getModel)
            updateTransactions();
        updateAccount();
    }

    private void getTransactionFromDatabase() {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.transactions/elements");
        String[] columns = {TransactionDBOpenHelper.TRANSACTION_ID,
                TransactionDBOpenHelper.TRANSACTION_TITLE,
                TransactionDBOpenHelper.TRANSACTION_DATE,
                TransactionDBOpenHelper.TRANSACTION_AMOUNT,
                TransactionDBOpenHelper.TRANSACTION_TYPE,
                TransactionDBOpenHelper.TRANSACTION_DESCRIPTION,
                TransactionDBOpenHelper.TRANSACTION_INTERVAL,
                TransactionDBOpenHelper.TRANSACTION_END_DATE,
                TransactionDBOpenHelper.TRANSACTION_ORGINAL_AMOUNT,
                TransactionDBOpenHelper.TRANSACTION_CHANGE};
        Cursor cursor = cr.query(uri, columns, null, null, null);

        while(cursor.moveToNext()){
            Transaction transaction = new Transaction();
            transaction.setId(cursor.getInt(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ID)));
            transaction.setTitle(cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_TITLE)));
            transaction.setType(TransactionType.getType(cursor.getInt(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_TYPE))));
            transaction.setDate(DataChecker.getDateFromService(cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_DATE))));
            transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_AMOUNT)));
            transaction.setItemDescription(cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION)));
            transaction.setTransactionInterval(cursor.getInt(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_INTERVAL)));
            transaction.setEndDate(DataChecker.getDateFromService(cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_END_DATE))));
            transaction.setOrginalAmount(cursor.getDouble(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ORGINAL_AMOUNT)));

            int typeOfChange = cursor.getInt(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_CHANGE));
            if(typeOfChange == TransactionDBOpenHelper.TRANSACTION_MODE_REMOVE)
                transaction.setDeleted(true);


            transactions.add(transaction);
            if(typeOfChange == TransactionDBOpenHelper.TRANSACTION_MODE_ADD)
                typeOfChangeList.add(TransactionDBOpenHelper.TRANSACTION_MODE_ADD);
            else if(typeOfChange == TransactionDBOpenHelper.TRANSACTION_MODE_EDIT)
                typeOfChangeList.add(TransactionDBOpenHelper.TRANSACTION_MODE_EDIT);
            else if (typeOfChange == TransactionDBOpenHelper.TRANSACTION_MODE_REMOVE)
                typeOfChangeList.add(TransactionDBOpenHelper.TRANSACTION_MODE_REMOVE);

//            Log.e("TRANSA", transaction.getId() + " " + transaction.getTitle() + " " + transaction.getDate() + " " + transaction.getAmount() + " " + transaction.getType());

//            if(!getModel) {
//                Log.e("A_T", "DA");
//                if (typeOfChange == TransactionDBOpenHelper.TRANSACTION_MODE_ADD) {
//                    (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionAddDone) this, transaction, context)).execute();
//                    Log.e("ADD", transaction.getId() + "");
//                } else if (typeOfChange == TransactionDBOpenHelper.TRANSACTION_MODE_EDIT) {
//                    (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionEditDone) this, transaction, context)).execute();
//                    Log.e("EDIT", transaction.getId() + "");
//                } else if (typeOfChange == TransactionDBOpenHelper.TRANSACTION_MODE_REMOVE) {
//                    (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionRemoveDone) this, transaction, context)).execute();
//                    Log.e("REMOVE", transaction.getId() + "");
//                }
//            }
        }

//        if(!getModel)
//            cr.delete(uri, null, null);
    }

    private void updateTransactions(){
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.transactions/elements");

        // Do not update remove and undo transactions
        for(int i=0; i<transactions.size(); i++){
            if(typeOfChangeList.get(i) == TransactionDBOpenHelper.TRANSACTION_MODE_ADD){
                for(int j=i-1; j>=0; j--){
                    if(transactions.get(j).getId() == transactions.get(i).getId() && typeOfChangeList.get(j) == TransactionDBOpenHelper.TRANSACTION_MODE_REMOVE){
                        transactions.remove(j);
                        typeOfChangeList.remove(j);
                        i--;
                        transactions.remove(i);
                        typeOfChangeList.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }

        // Do not update remove and undo transactions
        for(int i=0; i<transactions.size(); i++){
            if(typeOfChangeList.get(i) == TransactionDBOpenHelper.TRANSACTION_MODE_REMOVE){
                for(int j=i-1; j>=0; j--){
                    if(transactions.get(j).getId() == transactions.get(i).getId() && typeOfChangeList.get(j) == TransactionDBOpenHelper.TRANSACTION_MODE_ADD){
                        transactions.remove(j);
                        typeOfChangeList.remove(j);
                        i--;
                        transactions.remove(i);
                        typeOfChangeList.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }

        // Just once update transactions which are multiple times edited
        for(int i=transactions.size()-1; i>0; i--){
            if(typeOfChangeList.get(i) == TransactionDBOpenHelper.TRANSACTION_MODE_EDIT){
                for(int j=i-1; j>=0; j--){
                    if(transactions.get(j).getId() == transactions.get(i).getId() && typeOfChangeList.get(j) == TransactionDBOpenHelper.TRANSACTION_MODE_EDIT){
                        transactions.remove(j);
                        typeOfChangeList.remove(j);
                        i--;
                    }
                    else if(transactions.get(j).getId() == transactions.get(i).getId() && typeOfChangeList.get(j) == TransactionDBOpenHelper.TRANSACTION_MODE_ADD){
                        transactions.remove(j);
                        typeOfChangeList.remove(j);
                        i--;
                        typeOfChangeList.set(i, TransactionDBOpenHelper.TRANSACTION_MODE_ADD);
                        break;
                    }
                }
            }
        }

        for(int i=0; i<transactions.size(); i++){
            Log.e("A_T", "DA");
            if (typeOfChangeList.get(i) == TransactionDBOpenHelper.TRANSACTION_MODE_ADD) {
                (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionAddDone) this, transactions.get(i), context)).execute();
                Log.e("ADD", transactions.get(i).getId() + "");
            } else if (typeOfChangeList.get(i) == TransactionDBOpenHelper.TRANSACTION_MODE_EDIT) {
                (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionEditDone) this, transactions.get(i), context)).execute();
                Log.e("EDIT", transactions.get(i).getId() + "");
            } else if (typeOfChangeList.get(i) == TransactionDBOpenHelper.TRANSACTION_MODE_REMOVE) {
                (new TransactionEditInteractor((TransactionEditInteractor.OnTransactionRemoveDone) this, transactions.get(i), context)).execute();
                Log.e("REMOVE", transactions.get(i).getId() + "");
            }
        }

        TransactionDBOpenHelper.newTransationID = 0;
        cr.delete(uri, null, null);
    }

    private void updateAccount() {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.accounts/elements");
        String[] columns = {TransactionDBOpenHelper.ACCOUNT_ID,
                TransactionDBOpenHelper.ACCOUNT_BUDGET,
                TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT,
                TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT};
        Cursor cursor = cr.query(uri, columns, null, null, null);

        while (cursor.moveToNext()) {
            Account account = new Account(0, 0, 0, 0);
            account.setId(cursor.getInt(cursor.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_ID)));
            account.setBudget(cursor.getDouble(cursor.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_BUDGET)));
            account.setMonthLimit(cursor.getDouble(cursor.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT)));
            account.setTotalLimit(cursor.getDouble(cursor.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT)));

            this.account = account;

            if(!getModel) {
                Log.e("A_A", "DA");
                (new BudgetInteractor((BudgetInteractor.OnAccountSearchDone) this, context)).execute("Update", Double.toString(account.getBudget()));
                (new BudgetInteractor((BudgetInteractor.OnAccountSearchDone) this, context)).execute("Update", Double.toString(account.getMonthLimit()), Double.toString(account.getTotalLimit()));
            }
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public void onAddDone(Transaction transaction) {

    }

    @Override
    public void onEditDone(Transaction transaction) {

    }

    @Override
    public void onRemoveDone(Transaction transaction) {

    }

    @Override
    public void onDone(Account account) {
        // Nekako ukinuti offline mode
    }
}
