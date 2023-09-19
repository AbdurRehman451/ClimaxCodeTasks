package com.example.climaxtest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.climaxtest.R

class HobbyAdapter(private val hobbies: List<String>) : RecyclerView.Adapter<HobbyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hobby, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobby = hobbies[position]
        holder.bind(hobby)
    }

    override fun getItemCount(): Int {
        return hobbies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hobbyNameTextView: TextView = itemView.findViewById(R.id.hobbyNameTextView)

        fun bind(hobby: String) {
            hobbyNameTextView.text = hobby
            // You can bind other views or perform actions here as needed.
        }
    }
}
