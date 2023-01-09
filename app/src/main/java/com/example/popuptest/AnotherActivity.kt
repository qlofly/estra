package com.example.popuptest

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.popuptest.databinding.ActivityAddAccountsBinding
import com.example.popuptest.databinding.ActivityEditAccountBinding
import com.example.popuptest.databinding.ActivityMainBinding
import com.example.popuptest.databinding.ActivityTransactionBinding


class AddAccounts : Activity() {
    lateinit var accountsBinding: ActivityAddAccountsBinding
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_accounts)
        accountsBinding = ActivityAddAccountsBinding.inflate(layoutInflater)
        setContentView(accountsBinding.root)
        accountsBinding.button3.setOnClickListener{
            val db = DBHelper(this, null)
            val age = accountsBinding.editTextNumber.text.toString()
            val name = accountsBinding.editTextTextPersonName2.text.toString()
            if (name == ""){
                Toast.makeText(this, "ENTER ACCOUNT NAME", Toast.LENGTH_LONG).show()
            }
            else if (age == ""){
                db.addAccount(name, 0.toString())
                onBackPressed()
            }
            else{
                db.addAccount(name, age)
                Toast.makeText(this, name + " added to database", Toast.LENGTH_LONG).show()
                onBackPressed()
            }

        }
//        val bindingClass : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
//        accountsBinding.button3.layoutParams = bindingClass.plusMoneyButton.layoutParams
    }
}

class CreateTransaction : Activity(){
    lateinit var transactionBinding: ActivityTransactionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionBinding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(transactionBinding.root)
        val getData = getIntent().getStringExtra("plainText")
        transactionBinding.textView3.text = getData.toString()

    }
}

class EditAccount : Activity(){
    lateinit var editAccountBinding: ActivityEditAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editAccountBinding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(editAccountBinding.root)
        val accountDataText = getIntent().getStringExtra("nameAcc")
        val onlyName = accountDataText?.split(Regex("\n"))?.last().toString()
        val onlyAmount = accountDataText?.split(Regex("\n"))?.first().toString()
        editAccountBinding.textViewName.text = onlyName
        editAccountBinding.nameInput.setText(onlyName)
        editAccountBinding.amountInput.setText(onlyAmount)
        val db = DBHelper(this, null)
        editAccountBinding.saveButton.setOnClickListener(){
            val nameInput = editAccountBinding.nameInput.text.toString()
            val amountInput = editAccountBinding.amountInput.text.toString()
            if (nameInput == "" || amountInput == ""){
                Toast.makeText(this, "ENTER NAME OR AMOUNT", Toast.LENGTH_LONG).show()
            }
            else{
                db.updateAccountData(onlyName, nameInput, amountInput)
                onBackPressed()
            }
        }
        editAccountBinding.delButton.setOnClickListener(){
            val db = DBHelper(this, null)
            db.deleteAccountData(onlyName)
            Toast.makeText(this, "$onlyName success deleted", Toast.LENGTH_LONG).show()
            onBackPressed()
        }

    }
}
