package com.example.ugd1.memberGym

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd1.R
import com.example.ugd1.room.MemberGym
import kotlinx.android.synthetic.main.activity_member_gym_adapter.view.*

class MemberGymAdapter (private val memberGyms : ArrayList<MemberGym>, private val
listener: OnAdapterListener) :
    RecyclerView.Adapter<MemberGymAdapter.MemberGymViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MemberGymViewHolder {
        return MemberGymViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_member_gym_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MemberGymViewHolder, position:
        Int ) {
        val memberGym = memberGyms[position]
        holder.view.tvPersonalTrainer.text = memberGym.personalTrainer
        holder.view.tvPersonalTrainer.setOnClickListener {
            listener.onClick(memberGym)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(memberGym)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(memberGym)
        }
    }

    override fun getItemCount() = memberGyms.size
    inner class MemberGymViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<MemberGym>) {
        memberGyms.clear()
        memberGyms.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(memberGym: MemberGym)
        fun onUpdate(memberGym: MemberGym)
        fun onDelete(memberGym: MemberGym)

    }
}