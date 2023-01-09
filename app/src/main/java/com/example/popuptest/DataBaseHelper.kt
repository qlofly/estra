package com.example.popuptest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log



class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val firstQuery = ("CREATE TABLE " + TRANSACTION_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                DATE_TIME_TRANSACTION + " TEXT," +
                ACCOUNT_TRANSACTION + " TEXT," +
                AMOUNT_TRANSACTION + " TEXT," +
                CATEGORIES_TRANSACTION + " TEXT" + ")")
        val secondQuery = ("CREATE TABLE " + ACCOUNTS_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_ACCOUNTS_COl + " TEXT," +
                ACCOUNTS_AMOUNT_COL + " TEXT" + ")")
        val thirdQuery = ("CREATE TABLE " + CATIGORIES_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                SPENDING_COL + " TEXT," +
                INCOME_COL + " TEXT" + ")")

        db.execSQL(firstQuery)
        db.execSQL(secondQuery)
        db.execSQL(thirdQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        //  check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE_NAME)
        onCreate(db)
    }

    fun addAccount(nameAccount : String, amountAccount : String){

        val values = ContentValues()
        values.put(NAME_ACCOUNTS_COl, nameAccount)
        values.put(ACCOUNTS_AMOUNT_COL, amountAccount)
        val db = this.writableDatabase
        db.insert(ACCOUNTS_TABLE_NAME, null, values)
        db.close()
    }

    fun getMaxData(): Cursor? {

        val db = this.readableDatabase
        return db.rawQuery("SELECT name_accounts, amount_money FROM "+ ACCOUNTS_TABLE_NAME +" WHERE ID =(SELECT MAX(ID)  FROM "+ ACCOUNTS_TABLE_NAME+")", null)

    }

    fun updateAccountData(oldName: String, updName: String, updAmount: String){
        val db = this.readableDatabase
        db.execSQL("UPDATE $ACCOUNTS_TABLE_NAME SET $ACCOUNTS_AMOUNT_COL = '$updAmount', $NAME_ACCOUNTS_COl = '$updName' WHERE $NAME_ACCOUNTS_COl = '$oldName'")
    }

    fun deleteAccountData(nameAccount: String){
        val db = this.readableDatabase
        db.execSQL("DELETE FROM $ACCOUNTS_TABLE_NAME  WHERE $NAME_ACCOUNTS_COl = '$nameAccount'")
    }
    @SuppressLint("Recycle")
    fun getGlobalAmount(): String{
        val db = this.readableDatabase
        val query = db.rawQuery("SELECT amount_money FROM " + ACCOUNTS_TABLE_NAME, null)
        var amount = 0.00

        while (query.moveToNext()) {
            Log.d("AMOUNTEST", amount.toString())
            val countMoney = query.getString(query.getColumnIndexOrThrow(ACCOUNTS_AMOUNT_COL))
            amount += countMoney.toDouble()
        }
        db.close()
        fun Double.roundDecimal(digit: Int) = "%.${digit}f".format(this)
        return amount.roundDecimal(2)
    }

    @SuppressLint("Recycle")
    fun getNameAmount(): MutableList<String>{
        val db = this.readableDatabase
        val query = db.rawQuery("SELECT name_accounts,amount_money  FROM " + ACCOUNTS_TABLE_NAME, null)
        val data = mutableListOf<String>()

        while (query.moveToNext()) {
            val name = query.getString(query.getColumnIndexOrThrow(NAME_ACCOUNTS_COl))
            val count = query.getString(query.getColumnIndexOrThrow(ACCOUNTS_AMOUNT_COL))
            data.add( count +"\n\n\n" + name)
        }
        db.close()
        return data
    }



    companion object{
        private val DATABASE_NAME = "estra_transactions"

        private val DATABASE_VERSION = 1

        val  ACCOUNTS_TABLE_NAME = "accounts_table"
        val  TRANSACTION_TABLE_NAME = "transactions_table"
        val ID_COL = "id"

        val DATE_TIME_TRANSACTION = "datetime_transaction"
        val CATEGORIES_TRANSACTION = "catigories_transaction"
        val AMOUNT_TRANSACTION = "amount_transaction"
        val ACCOUNT_TRANSACTION = "account_transaction"

        val NAME_ACCOUNTS_COl = "name_accounts"
        val ACCOUNTS_AMOUNT_COL = "amount_money"

        val CATIGORIES_TABLE_NAME = "categories"
        val INCOME_COL = "income"
        val SPENDING_COL = "spending"
    }
}
