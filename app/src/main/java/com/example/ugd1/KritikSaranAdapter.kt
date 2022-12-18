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
import com.example.ugd1.model.KritikSaranModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class KritikSaranAdapter (private var kritikSaranList: List<KritikSaranModel>, context: Context) :
    RecyclerView.Adapter<KritikSaranAdapter.ViewHolder>(), Filterable {


    private var filteredkritikSaranList: MutableList<KritikSaranModel>
    private val context: Context

    init{
        filteredkritikSaranList = ArrayList(kritikSaranList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_kritik_saran, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredkritikSaranList.size
    }

    fun setKritikSaranList(kritikSaranList: Array<KritikSaranModel>){
        this.kritikSaranList = kritikSaranList.toList()
        filteredkritikSaranList = kritikSaranList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val kritikSaran = filteredkritikSaranList[position]
//        holder.tvId.text = member.id.toString()
        holder.tvNama.text = kritikSaran.nama
        holder.tvKritik.text = kritikSaran.kritik
        holder.tvSaran.text = kritikSaran.saran
        holder.tvNomorTelepon.text = kritikSaran.nomorTelepon

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data Kritik Saran ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_, _ ->
                    if(context is KritikSaranActivity){
                        context.deleteKritikSaran(
                            kritikSaran.id
                        )
                    }
                }
                .show()
        }
        holder.cvKritikSaran.setOnClickListener{
            val i = Intent(context, EditKritikSaran::class.java)
            i.putExtra("id",kritikSaran.id)
            if(context is KritikSaranActivity)
                context.startActivityForResult(i, KritikSaranActivity.LAUNCH_ADD_ACTIVITY)

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<KritikSaranModel> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(kritikSaranList)
                }else{
                    for(kritikSaran in kritikSaranList){
                        if(kritikSaran.nama.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(kritikSaran)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredkritikSaranList.clear()
                filteredkritikSaranList.addAll((filterResults.values as List<KritikSaranModel>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //        var tvId: TextView
        var tvNama: TextView
        var tvKritik: TextView
        var tvSaran: TextView
        var tvNomorTelepon: TextView
        var btnDelete: ImageButton
        var cvKritikSaran: CardView

        init{
//            tvId = itemView.findViewById(R.id.tv_id)
            tvNama = itemView.findViewById(R.id.tv_nama)
            tvKritik = itemView.findViewById(R.id.tv_kritik)
            tvSaran = itemView.findViewById(R.id.tv_saran)
            tvNomorTelepon = itemView.findViewById(R.id.tv_NomorTelepon)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvKritikSaran = itemView.findViewById(R.id.cv_kritikSaran)
        }
    }    
}