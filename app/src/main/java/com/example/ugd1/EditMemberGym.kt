package com.example.ugd1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd1.api.MemberGymApi
import com.example.ugd1.model.MemberGymModel
import com.example.ugd1.room.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_member_gym.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditMemberGym : AppCompatActivity() {

    private var etId: EditText? = null
    private var etPersonalTrainer: EditText? = null
    private var etMembership: EditText? = null
    private var edTanggal: EditText? = null
    private var edDurasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_member_gym)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etId = findViewById(R.id.et_id)
        etPersonalTrainer = findViewById(R.id.et_personalTrainer)
        etMembership = findViewById(R.id.et_membership)
        edTanggal = findViewById(R.id.et_tanggal)
        edDurasi = findViewById(R.id.et_durasi)
        layoutLoading = findViewById(R.id.layout_loading)
//
//        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle =findViewById<TextView>(R.id.tv_title)
        val id = intent.getIntExtra("id", -1)
        if(id== -1){
            tvTitle.setText("Tambah Member Gym")
            btnSave.setOnClickListener { createMahasiswa() }
        }else{
            tvTitle.setText("Edit Member Gym")
            getMahasiswaById(id)

            btnSave.setOnClickListener { updateMahasiswa(id) }
        }

    }

//    fun setExposedDropDownMenu(){
//        val adapterFakultas: ArrayAdapter<String> = ArrayAdapter<String>(this,
//            R.layout.item_list, FAKULTAS_LIST)
//        edFakultas!!.setAdapter(adapterFakultas)
//
//        val adapterProdi: ArrayAdapter<String> = ArrayAdapter<String>(this,
//            R.layout.item_list, PRODI_LIST)
//        edProdi!!.setAdapter(adapterProdi)
//    }

    private fun getMahasiswaById(id: Int){
        // Fungsi untuk menampilkan data mahasiswa berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, MemberGymApi.GET_BY_ID_URL + id, Response.Listener { response ->
//                val gson = Gson()
//                val member = gson.fromJson(response, MemberGym::class.java)

                var memberJo = JSONObject(response.toString())
                val member = memberJo.getJSONObject("data")

                etPersonalTrainer!!.setText(member.getString("personalTrainer"))
                etMembership!!.setText(member.getString("membership"))
                edTanggal!!.setText(member.getString("tanggal"))
                edDurasi!!.setText(member.getString("durasi"))

                Toast.makeText(this@EditMemberGym, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                setLoading(false)
            },  Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditMemberGym,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@EditMemberGym, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
        queue!!.add(stringRequest)
    }

    private fun createMahasiswa(){
        setLoading(true)

        val member = MemberGymModel(
            0,
            etPersonalTrainer!!.text.toString(),
            etMembership!!.text.toString(),
            edTanggal!!.text.toString(),
            edDurasi!!.text.toString()
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, MemberGymApi.ADD_URL, Response.Listener { response->
                val gson = Gson()
                val mahasiswa = gson.fromJson(response, MemberGym::class.java)

                if(mahasiswa!=null)
                    Toast.makeText(this@EditMemberGym, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditMemberGym,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@EditMemberGym, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers

                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(member)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        // Menambahkan request ke request queue
        queue!!.add(stringRequest)
    }

    private fun updateMahasiswa(id: Int){
        setLoading(true)

        val member = MemberGymModel(
            id,
            etPersonalTrainer!!.text.toString(),
            etMembership!!.text.toString(),
            edTanggal!!.text.toString(),
            edDurasi!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, MemberGymApi.UPDATE_URL + id, Response.Listener{ response ->
                val gson = Gson()

                val mahasiswa = gson.fromJson(response, MemberGymModel::class.java)

                if(mahasiswa != null)
                    Toast.makeText(this@EditMemberGym, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditMemberGym,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@EditMemberGym, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers

            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(member)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)

    }

    //Fungsi ini digunakan untuk menampilkan layout Loading

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