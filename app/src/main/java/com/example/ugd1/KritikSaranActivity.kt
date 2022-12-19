package com.example.ugd1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd1.api.KritikSaranApi
import com.example.ugd1.model.KritikSaranModel
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class KritikSaranActivity: AppCompatActivity(){
    private var srKritikSaran: SwipeRefreshLayout? =null
    private var adapter: KritikSaranAdapter? = null
    private var svKritikSaran: SearchView? =null
    private var layoutLoading: LinearLayout? =null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kritik_saran)

        queue =  Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srKritikSaran = findViewById(R.id.sr_kritikSaran)
        svKritikSaran = findViewById(R.id.sv_kritikSaran)

        srKritikSaran?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allKritikSaran() })
        svKritikSaran?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)

                return false
            }
        })

        val fabAdd = findViewById<Button>(R.id.btnAdd)
        fabAdd.setOnClickListener{
            val i = Intent(this@KritikSaranActivity, EditKritikSaran::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_kritikSaran)
        adapter = KritikSaranAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allKritikSaran()
    }

    private fun allKritikSaran(){
        srKritikSaran!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, KritikSaranApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val kritikSaranModel : Array<KritikSaranModel> = gson.fromJson(jsonData.toString(),Array<KritikSaranModel>::class.java)

                adapter!!.setKritikSaranList(kritikSaranModel)
                adapter!!.filter.filter(svKritikSaran!!.query)
                srKritikSaran!!.isRefreshing = false

                if(kritikSaranModel.isEmpty())
                    Toast.makeText(this@KritikSaranActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(this@KritikSaranActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srKritikSaran!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@KritikSaranActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@KritikSaranActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deleteKritikSaran(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, KritikSaranApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var kritikSaran = gson.fromJson(response, KritikSaranModel::class.java)
                if(kritikSaran != null)
                    Toast.makeText(this@KritikSaranActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                allKritikSaran()
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@KritikSaranActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@KritikSaranActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            // Menambahkan header pada request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == AppCompatActivity.RESULT_OK) allKritikSaran()
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}