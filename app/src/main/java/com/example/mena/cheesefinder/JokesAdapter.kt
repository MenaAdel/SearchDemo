package com.example.mena.cheesefinder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mena.cheesefinder.data.JokesResponse
import kotlinx.android.synthetic.main.list_item.view.*

class JokesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  var cheeses: List<JokesResponse> = listOf()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getItemCount() = cheeses.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    holder.itemView.textView.text = cheeses.get(position).value.joke
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}