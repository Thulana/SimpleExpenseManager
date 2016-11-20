package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by TK on 19/11/2016.
 */

public class PersistentTransactionDAO implements TransactionDAO {
    private DBHandler dbconnector;
    private SQLiteDatabase db;

    public PersistentTransactionDAO(Context context) {

        dbconnector=new DBHandler(context);
        db=dbconnector.getWritableDatabase();
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues values=new ContentValues();
        values.put("date",date.toString());
        values.put("accountNo",accountNo);
        values.put("expenseType",expenseType.toString());
        values.put("amount",amount);
        db.insert("Transaction",null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions=new ArrayList<>();
        Cursor resultSet = db.rawQuery("Select * from transaction",null);
        while(resultSet.moveToNext()){
            Transaction transaction=new Transaction(new Date(resultSet.getString(1)),resultSet.getString(2),ExpenseType.valueOf(resultSet.getString(3)),resultSet.getDouble(4));
            transactions.add(transaction);
        }
        return transactions;

    }
}
