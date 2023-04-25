package com.example.popuptest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.popuptest.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var bindingClass : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        bindingClass.addAccounts.setOnClickListener {
            val intent = Intent(this@MainActivity, AddAccounts::class.java)
            startActivity(intent)
        }
        bindingClass.plusMoneyButton.setOnClickListener{
            val transactionIntent = Intent(this@MainActivity, CreateTransaction::class.java)
            transactionIntent.putExtra("cmd", "income")
            startActivity(transactionIntent)
        }
        bindingClass.minusMoneyButton.setOnClickListener{
            val transactionIntent = Intent(this@MainActivity, CreateTransaction::class.java)
            transactionIntent.putExtra("cmd", "spending")
            startActivity(transactionIntent)
        }
    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        setContentView(bindingClass.root)
        val db = DBHelper(this, null)
        try {
            val cursor = db.getMaxData()
            cursor!!.moveToLast()
            bindingClass.linearLayout.removeAllViews()
            val ButtonData = db.getNameAmount()
            for (i in ButtonData){
                val button = Button(this)
                button.text = i
                button.background = bindingClass.addAccounts.background
                button.layoutParams = bindingClass.addAccounts.layoutParams
                button.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                button.setPadding(50, 0,50,0)

                button.setOnClickListener{
                    val intentEditAccount = Intent(this@MainActivity, EditAccount::class.java)
                    intentEditAccount.putExtra("nameAcc", i)
                    startActivity(intentEditAccount)
                }
                bindingClass.linearLayout.addView(button)
                cursor.close()
            }
            bindingClass.linearLayout.addView(bindingClass.addAccounts)
        } catch (e: Exception){
            Log.d("ERROR MESSAGE", e.toString())
        }

        val recyclerview = findViewById<RecyclerView>(R.id.recycleView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = db.getAllTransactions()

        // This will pass the ArrayList to our Adapter
        val adapter = TransactionAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        bindingClass.amount.text = db.getGlobalAmount()
        Log.d("Fragment1", "onResume")
//        //update count money with databases
//        //update all elements
    }

}
