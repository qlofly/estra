package com.example.popuptest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.popuptest.databinding.TransItemBinding

class TransactionAdapter: RecyclerView.Adapter<TransactionAdapter.TranHolder>() {
    val trList = ArrayList<Transactions_list>()
    class TranHolder(item: View): ViewHolder(item) {
        val binding = TransItemBinding.bind(item)
        fun bind(transactions: Transactions_list) = with(binding){
            trDate.text = transactions.date
            category.text = transactions.category
            moneyCount.text = transactions.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trans_item, parent, false)
        return TranHolder(view)
    }

    override fun onBindViewHolder(holder: TranHolder, position: Int) {
        holder.bind(trList[position])
    }

    override fun getItemCount(): Int {
        return trList.size
    }

    fun addTransaction(transactions: Transactions_list){
        trList.add(transactions)
        notifyDataSetChanged()
    }
}