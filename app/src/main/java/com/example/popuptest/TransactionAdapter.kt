package com.example.popuptest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.popuptest.databinding.TransItemBinding

class TransactionAdapter(private val transactionsList: ArrayList<DBHelper.Transaction>) : RecyclerView.Adapter<TransactionAdapter.TranHolder>() {    val trList = ArrayList<Transactions_list>()
    class TranHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = TransItemBinding.bind(item)
        fun bind(transactions: DBHelper.Transaction) = with(binding){
            trDate.text = transactions.date
            category.text = transactions.category
            moneyCount.text = transactions.amount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trans_item, parent, false)
        return TranHolder(view)
    }
    override fun onBindViewHolder(holder: TranHolder, position: Int) {
        holder.bind(transactionsList[position])
    }

    override fun getItemCount(): Int {
        return transactionsList.size
    }

//    override fun onBindViewHolder(holder: TranHolder, position: Int) {
//        holder.bind(trList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return trList.size
//    }
//
//    fun addTransaction(transactions: Transactions_list){
//        trList.add(transactions)
//        notifyDataSetChanged()
//    }
//
//    open class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val date: TextView = itemView.findViewById(R.id.trDate)
//        val categ: TextView = itemView.findViewById(R.id.category)
//        val price: TextView = itemView.findViewById(R.id.moneyCount)
//
//    }
}