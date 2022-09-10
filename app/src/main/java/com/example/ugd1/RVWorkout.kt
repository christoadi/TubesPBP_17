package com.example.ugd1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd1.entity.Workout

class RVWorkout(private val data: Array<Workout>) : RecyclerView.Adapter<RVWorkout.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        //Menghubungkan layout item recycler view kita
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_workout,parent,false)
        return viewHolder(itemView)
    }


    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvWorkout.text = currentItem.jenis

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvWorkout : TextView = itemView.findViewById(R.id.jenisWorkout)
    }
}