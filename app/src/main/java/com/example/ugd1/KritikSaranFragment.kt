package com.example.ugd1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_pesanan.*

class KritikSaranFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kritik_saran, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAdd: Button = view.findViewById(R.id.btnAdd)


        btnAdd.setOnClickListener(View.OnClickListener {
            val moveKritikSaran = Intent(this@KritikSaranFragment.context, KritikSaranActivity::class.java)
            startActivity(moveKritikSaran)
        })

        btnGlide.setOnClickListener(View.OnClickListener {
            val url = "https://picsum.photos/300"

            Glide.with(this)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageGlide)
        })
    }

}