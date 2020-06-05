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

    public static int getId(TransactionType type){
        for (Map.Entry<Integer, TransactionType> entry : map.entrySet()) {
            if(entry.getValue().equals(type))
                return entry.getKey();
        }
        return -1;
    }

    public static void getTransactionTypes() {
        map = new HashMap<Integer, TransactionType>();
        map.put(4, TransactionType.INDIVIDUALINCOME);
        map.put(5, TransactionType.INDIVIDUALPAYMENT);
        map.put(2, TransactionType.REGULARINCOME);
        map.put(1, TransactionType.REGULARPAYMENT);
        map.put(3, TransactionType.PURCHASE);
        if(ConnectivityBroadcastReceiver.isConnected)
            (new GetTransactionType()).execute();
    }

    public static boolean isRegular(TransactionType type){
        return TransactionType.REGULARPAYMENT.equals(type) || TransactionType.REGULARINCOME.equals(type);
    }

    public static boolean isIncome(TransactionType type){
        return TransactionType.INDIVIDUALINCOME.equals(type) || TransactionType.REGULARINCOME.equals(type);
    }
}
