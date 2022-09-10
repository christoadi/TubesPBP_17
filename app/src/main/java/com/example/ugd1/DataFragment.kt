package com.example.ugd1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd1.entity.Pelanggan

class DataFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter: RVPelanggan = RVPelanggan(Pelanggan.userData)

        //Menghubungkan rvMahasiswa dengan recycler view yang ada pada layout
        val rvData : RecyclerView = view.findViewById(R.id.rv_data)

        //Set layout manager dari recycler view
        rvData.layoutManager = layoutManager

        //tidak mengubah size recycler view jika terdapat item ditambahkan atau dikurangkan
        rvData.setHasFixedSize(true)

        //Set Adapter dari recycler view.
        rvData.adapter = adapter
    }
}