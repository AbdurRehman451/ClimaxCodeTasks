package com.example.climaxtest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climaxtest.R
import com.example.climaxtest.fragments.HomeFragment
import com.example.climaxtest.fragments.HomeFragmentDirections
import com.example.climaxtest.room.DataClass
import kotlinx.android.synthetic.main.recyler_list.view.*

class DataAdapter(val list : List<DataClass>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    class ViewHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyler_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.recyler_list_Title.text = list[position].name
        holder.view.recyler_age.text = list[position].age.toString()

        val hobbyAdapter = HobbyAdapter(list[position].hobbies)
        val layoutManager = LinearLayoutManager(holder.view.context)
        holder.view.recyler_hobbies.layoutManager = layoutManager
        holder.view.recyler_hobbies.adapter = hobbyAdapter

        Glide.with(holder.view)
            .load(list[position].imageUrl) // Replace with the URL or resource of your image
            .placeholder(R.drawable.ic_done) // Placeholder image while loading
            .error(R.drawable.ic_add) // Image to display if loading fails
            .into(holder.view.imageview)

        holder.view.mRelativerecycler.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddDataFragment()
            action.data = list[position]
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
      return  list.size
    }
}