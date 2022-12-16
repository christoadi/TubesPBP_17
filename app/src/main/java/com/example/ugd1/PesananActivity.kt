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
import com.example.ugd1.api.PesananApi
import com.example.ugd1.model.PesananModel
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PesananActivity : AppCompatActivity(){
    private var srPesanan: SwipeRefreshLayout? =null
    private var adapter: PesananAdapter? = null
    private var svPesanan: SearchView? =null
    private var layoutLoading: LinearLayout? =null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        queue =  Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srPesanan = findViewById(R.id.sr_pesanan)
        svPesanan = findViewById(R.id.sv_pesanan)

        srPesanan?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allPesanan() })
        svPesanan?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
            val i = Intent(this@PesananActivity, EditPesanan::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_pesanan)
        adapter = PesananAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allPesanan()
    }

    private fun allPesanan(){
        srPesanan!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PesananApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val pesananModel : Array<PesananModel> = gson.fromJson(jsonData.toString(),Array<PesananModel>::class.java)

                adapter!!.setPesananList(pesananModel)
                adapter!!.filter.filter(svPesanan!!.query)
                srPesanan!!.isRefreshing = false

                if(pesananModel.isEmpty())
                    Toast.makeText(this@PesananActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(this@PesananActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srPesanan!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PesananActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@PesananActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deletePesanan(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, PesananApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var pesanan = gson.fromJson(response, PesananModel::class.java)
                if(pesanan != null)
                    Toast.makeText(this@PesananActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                allPesanan()
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PesananActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@PesananActivity, e.message, Toast.LENGTH_SHORT).show()
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
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == AppCompatActivity.RESULT_OK) allPesanan()
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