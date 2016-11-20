package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by TK on 19/11/2016.
 */

public class PersistentAccountDAO implements AccountDAO {
    private DBHandler dbconnector;
    private SQLiteDatabase db;

    public PersistentAccountDAO(Context context) {
        dbconnector=new DBHandler(context);
        db=dbconnector.getWritableDatabase();

    }
    @Override
    public List<String> getAccountNumbersList() {
        String sql="SELECT accountNo FROM account";
        Cursor cursor=db.rawQuery(sql,null);
        List<String> accountNumberList=new ArrayList<>();
        while(cursor.moveToNext()){
            String accountNo=cursor.getString(1);

            accountNumberList.add(accountNo);
        }
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor resultSet = db.rawQuery("Select * from Account",null);
        List<Account> accountList=new ArrayList<>();
        while(resultSet.moveToNext()){
            String accountNo=resultSet.getString(1);
            String bank=resultSet.getString(2);
            String accountHolder=resultSet.getString(3);
            double balance=resultSet.getDouble(4);
            Account account=new Account(accountNo,bank,accountHolder,balance);
            accountList.add(account);
        }
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = db.rawQuery("Select * from Account where accountNo ="+accountNo+"",null);
        resultSet.moveToFirst();
        if(resultSet.getCount()>0){
            return new Account(resultSet.getString(1),resultSet.getString(1),resultSet.getString(3),resultSet.getDouble(4));

        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        ContentValues values=new ContentValues();
        values.put("accountNo",account.getAccountNo());
        values.put("bankName",account.getBankName());
        values.put("accountHolderName",account.getAccountHolderName());
        values.put("balance",account.getBalance());
        db.insert("Account",null,values);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String [] args={accountNo};
        int result=db.delete("Account","accountNo ?",args);
        if(result==0){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        ContentValues values=new ContentValues();
        String [] args={accountNo};
        values.put("balance",account.getBalance());
        int result =db.update("account",values,"accountNo ?",args);

    }
}
