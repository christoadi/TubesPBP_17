package com.example.ugd1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd1.model.MemberGymModel
import com.example.ugd1.room.MemberGym
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_member_gym_adapter.view.*
import java.util.*
import kotlin.collections.ArrayList

class MemberGymAdapter(private var memberGymList: List<MemberGymModel>, context: Context) :
    RecyclerView.Adapter<MemberGymAdapter.ViewHolder>(), Filterable {

    private var filteredMemberList: MutableList<MemberGymModel>
    private val context: Context

    init{
        filteredMemberList = ArrayList(memberGymList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_member, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredMemberList.size
    }

    fun setMemberGymList(memberGymList: Array<MemberGymModel>){
        this.memberGymList = memberGymList.toList()
        filteredMemberList = memberGymList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val member = filteredMemberList[position]
//        holder.tvId.text = member.id.toString()
        holder.tvPersonalTrainer.text = member.personalTrainer
        holder.tvMembership.text = member.membership
        holder.tvTanggal.text = member.tanggal
        holder.tvDurasi.text = member.durasi

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data mahasiswa ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_, _ ->
                    if(context is MemberGymActivity){
                        context.deleteMemberGym(
                            member.id
                        )
                    }
                }
                .show()
        }
        holder.cvMahasiswa.setOnClickListener{
            val i = Intent(context, EditMemberGym::class.java)
            i.putExtra("id",member.id)
            if(context is MemberGymActivity)
                context.startActivityForResult(i, MemberGymActivity.LAUNCH_ADD_ACTIVITY)

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<MemberGymModel> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(memberGymList)
                }else{
                    for(member in memberGymList){
                        if(member.personalTrainer.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(member)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredMemberList.clear()
                filteredMemberList.addAll((filterResults.values as List<MemberGymModel>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        var tvId: TextView
        var tvPersonalTrainer: TextView
        var tvMembership: TextView
        var tvTanggal: TextView
        var tvDurasi: TextView
        var btnDelete: ImageButton
        var cvMahasiswa: CardView

        init{
//            tvId = itemView.findViewById(R.id.tv_id)
            tvPersonalTrainer = itemView.findViewById(R.id.tv_personalTrainer)
            tvMembership = itemView.findViewById(R.id.tv_membership)
            tvTanggal = itemView.findViewById(R.id.tv_tanggal)
            tvDurasi = itemView.findViewById(R.id.tv_durasi)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvMahasiswa = itemView.findViewById(R.id.cv_mahasiswa)
        }
    }

}