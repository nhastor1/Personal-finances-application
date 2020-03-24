package ba.unsa.etf.rma.rma20hastornedim52;

import java.util.ArrayList;
import java.util.List;

public enum TransactionType {
    INDIVIDUALPAYMENT("INDIVIDUALPAYMENT"), REGULARPAYMENT("REGULARPAYMENT"), PURCHASE("PURCHASE"),
    INDIVIDUALINCOME("INDIVIDUALINCOME"), REGULARINCOME("REGULARINCOME");

    private String name;
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
}
