package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TransactionsDB.db";
    public static final int DATABASE_VERSION = 1;

    public TransactionDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TransactionDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TRANSACTION_TABLE = "transactions";
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_INTERNAL_ID = "_id";
    public static final String TRANSACTION_TITLE = "title";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_TYPE = "type";
    public static final String TRANSACTION_DESCRIPTION = "description";
    public static final String TRANSACTION_INTERVAL = "interval";
    public static final String TRANSACTION_END_DATE = "endDate";
    public static final String TRANSACTION_ORGINAL_AMOUNT = "orginalAmount";
    public static final String TRANSACTION_CHANGE = "change";
    public static final int TRANSACTION_MODE_ADD = 1;
    public static final int TRANSACTION_MODE_EDIT = 2;
    public static final int TRANSACTION_MODE_REMOVE = 3;

    public static int newTransationID = 0;

    private static final String TRANSACTION_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " ("  + TRANSACTION_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSACTION_ID + " INTEGER, "
                    + TRANSACTION_TITLE + " TEXT NOT NULL, "
                    + TRANSACTION_DATE + " TEXT NOT NULL, "
                    + TRANSACTION_AMOUNT + " REAL NOT NULL, "
                    + TRANSACTION_TYPE + " INTEGER NOT NULL, "
                    + TRANSACTION_DESCRIPTION + " TEXT, "
                    + TRANSACTION_INTERVAL + " INTEGER, "
                    + TRANSACTION_END_DATE + " TEXT, "
                    + TRANSACTION_ORGINAL_AMOUNT + " REAL, "
                    + TRANSACTION_CHANGE + " INTEGER NOT NULL);";

    private static final String TRANSACTION_DROP = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;

    public static final String ACCOUNT_TABLE = "accounts";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_BUDGET = "budget";
    public static final String ACCOUNT_TOTAL_LIMIT = "totalLimit";
    public static final String ACCOUNT_MONTH_LIMIT = "monthLimit";

    private static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " ("  + ACCOUNT_ID + " INTEGER PRIMARY KEY, "
                    + ACCOUNT_BUDGET + " REAL NOT NULL, "
                    + ACCOUNT_TOTAL_LIMIT + " REAL NOT NULL, "
                    + ACCOUNT_MONTH_LIMIT + " REAL NOT NULL);";

    private static final String ACCOUNT_DROP = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRANSACTION_TABLE_CREATE);
        db.execSQL(ACCOUNT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TRANSACTION_DROP);
        db.execSQL(ACCOUNT_DROP);
        onCreate(db);
    }
}
