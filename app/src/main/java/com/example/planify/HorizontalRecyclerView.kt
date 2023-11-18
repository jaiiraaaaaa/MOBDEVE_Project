package com.example.planify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
class HorizontalRecyclerView : RecyclerView.Adapter<HorizontalRecyclerView.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.minimized_card, parent, false)
        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
        return 20 // temp value
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}