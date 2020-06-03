package ba.unsa.etf.rma.rma20hastornedim52;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TransactionContentProvider extends ContentProvider {

    private static final int ALLROWS =1;
    private static final int ONEROW = 2;
    private static final UriMatcher uM;

    static {
        uM = new UriMatcher(UriMatcher.NO_MATCH);
        uM.addURI("rma.provider.transactions","elements",ALLROWS);
        uM.addURI("rma.provider.transactions","elements/#",ONEROW);
    }

    TransactionDBOpenHelper tHelper;

    @Override
    public boolean onCreate() {
        tHelper = new TransactionDBOpenHelper(getContext(),
                TransactionDBOpenHelper.DATABASE_NAME,null,
                TransactionDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArg, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try{
            database= tHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database= tHelper.getReadableDatabase();
        }
        String groupby=null;
        String having=null;
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();

        switch (uM.match(uri)){
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                squery.appendWhere(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID+"="+idRow);
            default:break;
        }
        squery.setTables(TransactionDBOpenHelper.TRANSACTION_TABLE);
        Cursor cursor = squery.query(database,projection,selection,selectionArg,groupby,having,sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uM.match(uri)){
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.rma.elemental";
            case ONEROW:
                return "vnd.android.cursor.item/vnd.rma.elemental";
            default:
                throw new IllegalArgumentException("Unsuported uri: "+uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database;
        try{
            database=tHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=tHelper.getReadableDatabase();
        }
        long id = database.insert(TransactionDBOpenHelper.TRANSACTION_TABLE, null, contentValues);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database;
        try{
            database=tHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=tHelper.getReadableDatabase();
        }

        switch (uM.match(uri)) {
            case ALLROWS:
                selection = "1";
                break;
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                selection = TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID + "=" + idRow;
                break;
            default:
                throw new IllegalArgumentException("Unsuported uri: "+uri.toString());
        }

        int rowsDeleted = database.delete(TransactionDBOpenHelper.TRANSACTION_TABLE, selection, selectionArgs);

        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database;
        try{
            database=tHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=tHelper.getReadableDatabase();
        }

        switch (uM.match(uri)) {
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                selection = TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID + "=" + idRow;
                break;
            default:
                throw new IllegalArgumentException("Unsuported uri: "+uri.toString());
        }

        int rowsUpdated = database.update(TransactionDBOpenHelper.TRANSACTION_TABLE, contentValues, selection, selectionArgs);

        if(rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

}
