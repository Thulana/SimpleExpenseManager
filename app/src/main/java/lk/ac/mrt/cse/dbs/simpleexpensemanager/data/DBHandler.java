package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TK on 19/11/2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE Account (accountNo VARCHAR,bankName VARCHAR, accountHolderName VARCHAR, balance DOUBLE ); " +
                    "CREATE TABLE Transaction (accountNo VARCHAR,date DATE,expenseType VARCHAR, balance DOUBLE );";

    public DBHandler(Context context){
        super(context, "140285M", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
