package com.example.ugd1

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import com.example.ugd1.entity.Pelanggan

class RVPelanggan(private val data: Array<Pelanggan>) : RecyclerView.Adapter<RVPelanggan.viewHolder>(){

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): viewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.rv_data,parent,false)
        return viewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvNama.text= currentItem.nama
        holder.tvNomorTelepon.text = currentItem.noTelp
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvNama : TextView = itemView.findViewById(R.id.tvNama)
        val tvNomorTelepon : TextView = itemView.findViewById(R.id.tvNomorTelepon)
    }
}