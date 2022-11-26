package com.example.ugd1

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd1.api.MemberGymApi
import com.example.ugd1.model.MemberGymModel
//import com.example.ugd1.room.Constant
//import com.example.ugd1.room.MemberGym
//import com.example.ugd1.room.MemberGymDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_member_gym.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MemberGymActivity : AppCompatActivity() {
    private var srMahasiswa: SwipeRefreshLayout? =null
    private var adapter: MemberGymAdapter? = null
    private var svMahasiswa: SearchView? =null
    private var layoutLoading: LinearLayout? =null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_gym)

        queue =  Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srMahasiswa = findViewById(R.id.sr_mahasiswa)
        svMahasiswa = findViewById(R.id.sv_mahasiswa)

        srMahasiswa?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allMahasiswa() })
        svMahasiswa?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
            val i = Intent(this@MemberGymActivity, EditMemberGym::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_mahasiswa)
        adapter = MemberGymAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allMahasiswa()
    }

    private fun allMahasiswa(){
        srMahasiswa!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, MemberGymApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val memberGymModel : Array<MemberGymModel> = gson.fromJson(jsonData.toString(),Array<MemberGymModel>::class.java)

                adapter!!.setMemberGymList(memberGymModel)
                adapter!!.filter.filter(svMahasiswa!!.query)
                srMahasiswa!!.isRefreshing = false

                if(!memberGymModel.isEmpty())
                    Toast.makeText(this@MemberGymActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(this@MemberGymActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srMahasiswa!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MemberGymActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@MemberGymActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteMemberGym(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, MemberGymApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var mahasiswa = gson.fromJson(response, MemberGymModel::class.java)
                if(mahasiswa != null)
                    Toast.makeText(this@MemberGymActivity, "Data Berhasil Dihapus",Toast.LENGTH_SHORT).show()
                allMahasiswa()
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MemberGymActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@MemberGymActivity, e.message, Toast.LENGTH_SHORT).show()
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
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allMahasiswa()
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