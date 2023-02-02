package com.example.popuptest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.marginLeft
import com.example.popuptest.databinding.ActivityAddAccountsBinding
import com.example.popuptest.databinding.ActivityCategoriesBinding
import com.example.popuptest.databinding.ActivityEditAccountBinding
import com.example.popuptest.databinding.ActivityTransactionBinding


class AddAccounts : Activity() {
    lateinit var accountsBinding: ActivityAddAccountsBinding
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

class CreateTransaction : Activity() {
    lateinit var transactionBinding: ActivityTransactionBinding
    @SuppressLint("ResourceType")
    val db = DBHelper(this, null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionBinding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(transactionBinding.root)
        val getData = getIntent().getStringExtra("cmd")
        Log.d("spending ERROR", getData.toString())
        val listOfPlus = db.getallCaterogies(getData.toString(), )
        Log.d("ListOfPlus", listOfPlus.toString())
        val spinner = findViewById<View>(transactionBinding.spinner.id) as Spinner
        val spinnerAdapter = ArrayAdapter<String>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                listOfPlus)
        spinnerAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        transactionBinding.textView3.text = getData.toString()
        transactionBinding.buttonCreateCatig.setOnClickListener{
            val intent = Intent(this@CreateTransaction, EditCategory::class.java)
            intent.putExtra("cmd", getData)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val getData = getIntent().getStringExtra("cmd")
        Log.d("GetData in ON RESUME", getData.toString())
        val listOfPlus = db.getallCaterogies(getData.toString())
        val spinner = findViewById<View>(transactionBinding.spinner.id) as Spinner
        val spinnerAdapter = ArrayAdapter<String>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                listOfPlus
            )
        spinnerAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        transactionBinding.textView3.text = getData.toString()

    }
}

class EditCategory: Activity(){
    val db = DBHelper(this, null)
    lateinit var categoriesBinding: ActivityCategoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoriesBinding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(categoriesBinding.root)
        val getData = getIntent().getStringExtra("cmd")
        categoriesBinding.addCategoriesButton.setOnClickListener() {
            val inputName = categoriesBinding.categoriesNameText.text.toString()
            if (inputName == "") {
                Toast.makeText(this, "ENTER NAME", Toast.LENGTH_LONG).show()
            }
            if (inputName != "") {
                db.writeCategories(getData.toString(), inputName)
                Toast.makeText(this, "$inputName success create", Toast.LENGTH_LONG).show()
                onBackPressed()
            }
        }

    }
    override fun onResume() {
        super.onResume()
        setContentView(categoriesBinding.root)
        val getData = getIntent().getStringExtra("cmd")
        val listOfCategories = db.getallCaterogies(getData.toString())
        for(categoriesName in listOfCategories){
            val labelElement = TextView(this)
            val delButton = Button(this)
            labelElement.text = categoriesName
            delButton.text = "Delete"
            delButton.setOnClickListener(){
                db.deleteCategories(categoriesName, getData.toString())
                Toast.makeText(this, "$categoriesName has been deleted", Toast.LENGTH_LONG).show()
                onBackPressed()
            }
            val layout2 = LinearLayout(this)
            layout2.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layout2.orientation = LinearLayout.HORIZONTAL
            layout2.addView(labelElement)
            layout2.addView(delButton)
            categoriesBinding.listOfCategories.addView(layout2)


        }
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
