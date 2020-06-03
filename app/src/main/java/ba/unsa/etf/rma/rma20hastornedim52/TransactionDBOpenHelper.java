package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
    public static final String TRANSACTION_TYPE_CHANGE = "typeChange";

    private static final String TRANSACTION_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " ("  + TRANSACTION_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSACTION_ID + " INTEGER UNIQUE, "
                    + TRANSACTION_TITLE + " TEXT NOT NULL, "
                    + TRANSACTION_DATE + " TEXT NOT NULL, "
                    + TRANSACTION_AMOUNT + " REAL NOT NULL, "
                    + TRANSACTION_TYPE + " INTEGER NOT NULL, "
                    + TRANSACTION_DESCRIPTION + " TEXT, "
                    + TRANSACTION_INTERVAL + " INTEGER, "
                    + TRANSACTION_END_DATE + " TEXT, "
                    + TRANSACTION_ORGINAL_AMOUNT + " REAL, "
                    + TRANSACTION_TYPE_CHANGE + " INTEGER NOT NULL);";

    private static final String TRANSACTION_DROP = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE_CREATE;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRANSACTION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TRANSACTION_DROP);
        onCreate(db);
    }
}
