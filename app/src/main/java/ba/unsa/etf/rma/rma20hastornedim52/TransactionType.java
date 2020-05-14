package ba.unsa.etf.rma.rma20hastornedim52;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TransactionType {
    INDIVIDUALPAYMENT("INDIVIDUALPAYMENT"), REGULARPAYMENT("REGULARPAYMENT"), PURCHASE("PURCHASE"),
    INDIVIDUALINCOME("INDIVIDUALINCOME"), REGULARINCOME("REGULARINCOME");

    private String name;
    public static Map<Integer, TransactionType> map;

    private TransactionType(String type) {
        this.name = type;
    }

    @Override
    public String toString(){
        return name;
    }

    public static List<TransactionType> getAllTypes(){
        return new ArrayList<TransactionType>(){
            {
                add(TransactionType.INDIVIDUALPAYMENT);
                add(TransactionType.REGULARPAYMENT);
                add(TransactionType.INDIVIDUALINCOME);
                add(TransactionType.REGULARINCOME);
                add(TransactionType.PURCHASE);
            }
        };
    }

    public static TransactionType getType(String s){
        switch (s){
            case "INDIVIDUALPAYMENT":
                return TransactionType.INDIVIDUALPAYMENT;
            case "Individual payment":
                return TransactionType.INDIVIDUALPAYMENT;
            case "REGULARPAYMENT":
                return TransactionType.REGULARPAYMENT;
            case "Regular payment":
                return TransactionType.REGULARPAYMENT;
            case "INDIVIDUALINCOME":
                return TransactionType.INDIVIDUALINCOME;
            case "Individual income":
                return TransactionType.INDIVIDUALINCOME;
            case "REGULARINCOME":
                return TransactionType.REGULARINCOME;
            case "Regular income":
                return TransactionType.REGULARINCOME;
            default:
                return TransactionType.PURCHASE;
        }
    }

    public static TransactionType getType(int transactionTypeId) {
        if(map==null)
            return TransactionType.PURCHASE;

        return map.get(transactionTypeId);
    }

    public static void getTransactionTypes() {
        (new GetTransactionType()).execute();
    }
}
