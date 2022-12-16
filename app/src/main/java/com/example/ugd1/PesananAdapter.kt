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
import com.example.ugd1.model.PesananModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class PesananAdapter (private var pesananList: List<PesananModel>, context: Context) :
    RecyclerView.Adapter<PesananAdapter.ViewHolder>(), Filterable {

        private var filteredPesananList: MutableList<PesananModel>
        private val context: Context

        init{
            filteredPesananList = ArrayList(pesananList)
            this.context=context
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_pesanan, parent, false)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return filteredPesananList.size
        }

        fun setPesananList(pesananList: Array<PesananModel>){
            this.pesananList = pesananList.toList()
            filteredPesananList = pesananList.toMutableList()
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int){
            val pesanan = filteredPesananList[position]
//        holder.tvId.text = member.id.toString()
            holder.tvNamaPemesan.text = pesanan.namaPemesan
            holder.tvNamaBarang.text = pesanan.namaBarang
            holder.tvJumlah.text = pesanan.jumlah
            holder.tvTanggalAmbil.text = pesanan.tanggalAmbil

            holder.btnDelete.setOnClickListener{
                val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                    .setMessage("Apakah anda yakin ingin menghapus data pesanan ini?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Hapus"){_, _ ->
                        if(context is PesananActivity){
                            context.deletePesanan(
                                pesanan.id
                            )
                        }
                    }
                    .show()
            }
            holder.cvPesanan.setOnClickListener{
                val i = Intent(context, EditPesanan::class.java)
                i.putExtra("id",pesanan.id)
                if(context is PesananActivity)
                    context.startActivityForResult(i, PesananActivity.LAUNCH_ADD_ACTIVITY)

            }
        }

        override fun getFilter(): Filter {
            return object : Filter(){
                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    val charSequenceString = charSequence.toString()
                    val filtered : MutableList<PesananModel> = java.util.ArrayList()
                    if(charSequenceString.isEmpty()){
                        filtered.addAll(pesananList)
                    }else{
                        for(pesanan in pesananList){
                            if(pesanan.namaPemesan.lowercase(Locale.getDefault())
                                    .contains(charSequenceString.lowercase(Locale.getDefault()))
                            ) filtered.add(pesanan)

                        }
                    }
                    val filterResults = FilterResults()
                    filterResults.values = filtered
                    return filterResults
                }

                override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                    filteredPesananList.clear()
                    filteredPesananList.addAll((filterResults.values as List<PesananModel>))
                    notifyDataSetChanged()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            //        var tvId: TextView
            var tvNamaPemesan: TextView
            var tvNamaBarang: TextView
            var tvJumlah: TextView
            var tvTanggalAmbil: TextView
            var btnDelete: ImageButton
            var cvPesanan: CardView

            init{
//            tvId = itemView.findViewById(R.id.tv_id)
                tvNamaPemesan = itemView.findViewById(R.id.tv_namaPemesan)
                tvNamaBarang = itemView.findViewById(R.id.tv_namaBarang)
                tvJumlah = itemView.findViewById(R.id.tv_jumlah)
                tvTanggalAmbil = itemView.findViewById(R.id.tv_tanggalAmbil)
                btnDelete = itemView.findViewById(R.id.btn_delete)
                cvPesanan = itemView.findViewById(R.id.cv_pesanan)
            }
        }
}