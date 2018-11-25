package com.example.mena.cheesefinder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*

class CheeseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  var cheeses: List<String> = listOf()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getItemCount() = cheeses.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    holder.itemView.textView.text = cheeses[position]
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}