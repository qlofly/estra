package com.example.popuptest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.popuptest.databinding.ActivityAddAccountsBinding
import com.example.popuptest.databinding.ActivityCategoriesBinding
import com.example.popuptest.databinding.ActivityEditAccountBinding
import com.example.popuptest.databinding.ActivityTransactionBinding
import java.text.SimpleDateFormat
import java.util.*


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
            val listDbAccName = db.getAllNameAccounts()
            if (name == ""){
                Toast.makeText(this, "ENTER ACCOUNT NAME", Toast.LENGTH_LONG).show()
            }
            else if (name in listDbAccName){
                Toast.makeText(this, "$name already used", Toast.LENGTH_LONG).show()
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
            val listDbAccName = db.getAllNameAccounts()
            if (nameInput == "" || amountInput == ""){
                Toast.makeText(this, "ENTER NAME OR AMOUNT", Toast.LENGTH_LONG).show()
            }
            else if (nameInput in listDbAccName){
                Toast.makeText(this, "$nameInput already used", Toast.LENGTH_LONG).show()
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

class CreateTransaction : Activity() {
    lateinit var transactionBinding: ActivityTransactionBinding
    @SuppressLint("ResourceType")
    val db = DBHelper(this, null)
    lateinit var selectedAccount:String
    lateinit var selectedCategory:String
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
//        val selectedCategory = selectedSpinnerCatItem as String
        lateinit var selectedAccount: String
        super.onCreate(savedInstanceState)
        transactionBinding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(transactionBinding.root)
        val getData = getIntent().getStringExtra("cmd")

        //импорт названия счетов в спинер
        val accountsAllName = db.getAllNameAccounts()
        val accountSpinner = findViewById<View>(transactionBinding.accountSpinner.id) as Spinner
        val accountSpinnerAdapter = ArrayAdapter<String>(
            this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            accountsAllName)
        accountSpinnerAdapter.setDropDownViewResource(
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        accountSpinner.adapter = accountSpinnerAdapter
        accountSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedAccount = parent.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        //импорт данных в спиннер категорий
        val listOfPlus = db.getallCaterogies(getData.toString())
        val spinner = findViewById<View>(transactionBinding.spinner.id) as Spinner
        val spinnerAdapter = ArrayAdapter<String>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                listOfPlus)
        spinnerAdapter.setDropDownViewResource(
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedCategory = parent.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        transactionBinding.textView3.text = getData.toString()
        transactionBinding.buttonSaveTransaction.setOnClickListener(){
            //сохранять все данные из полей и выходить обратно
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
            val currentDate = sdf.format(Date())
            val transactionData = arrayOf(
                currentDate,
                selectedAccount,
                transactionBinding.amountText.text.toString(),
                selectedCategory,
                transactionBinding.commentText.text.toString()
            )
            db.createTransaction(transactionData)
            Toast.makeText(this, "Transaction created", Toast.LENGTH_LONG).show()
//            Log.d("MONEY", transactionBinding.amountText.text.toString())
//            Log.d("COMMNT", transactionBinding.commentText.text.toString())
//            Log.d("CATEGR", selectedCategory)
//            Log.d("ACCNT", selectedAccount)
            onBackPressed()
        }
        transactionBinding.buttonCreateCatig.setOnClickListener{
            val intent = Intent(this@CreateTransaction, EditCategory::class.java)
            intent.putExtra("cmd", getData)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val getData = getIntent().getStringExtra("cmd")
        val listOfPlus = db.getallCaterogies(getData.toString())
        val spinner = findViewById<View>(transactionBinding.spinner.id) as Spinner
        val spinnerAdapter = ArrayAdapter<String>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                listOfPlus
            )
        spinnerAdapter.setDropDownViewResource(
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
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
            val exampleText = categoriesBinding.exampleLinearCat.findViewById<TextView>(R.id.textView11)
            val exampleButton = categoriesBinding.exampleLinearCat.findViewById<TextView>(R.id.button)
//            val layoutExample = categoriesBinding.exampleLinearCat.findViewById<TextView>(R.id.exampleLinearCat)
            val labelElement = TextView(this)
            val delButton = Button(this)
            labelElement.text = categoriesName
            labelElement.layoutParams = exampleText.layoutParams
            delButton.text = "Delete"
            delButton.layoutParams = exampleButton.layoutParams
            delButton.setOnClickListener(){
                db.deleteCategories(categoriesName, getData.toString())
                Toast.makeText(this, "$categoriesName has been deleted", Toast.LENGTH_LONG).show()
                onBackPressed()
            }
            val layout2 = LinearLayout(this)
            layout2.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layout2.orientation = LinearLayout.HORIZONTAL
//            layout2.layoutParams = layoutExample.layoutParams
            layout2.addView(labelElement)
            layout2.addView(delButton)
            categoriesBinding.listOfCategories.addView(layout2)


        }
    }
}


