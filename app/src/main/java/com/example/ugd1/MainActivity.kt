package com.example.ugd1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isEmpty
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd1.api.UserApi
import com.example.ugd1.model.UserModel
import com.example.ugd1.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var mBundle: Bundle
    var tempUsername: String = "admin"
    var tempPass: String = "admin"
    private var queue: RequestQueue? = null

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setTitle("User Login")

        queue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnClear: Button = findViewById(R.id.btnClear)

        if(intent.getBundleExtra("register")!=null){
            mBundle = intent.getBundleExtra("register")!!
            tempUsername = mBundle!!.getString("username")!!
            tempPass = mBundle!!.getString("password")!!
            println(tempUsername)
            inputUsername.editText?.setText(tempUsername)
            inputPassword.editText?.setText(tempPass)
        }
        btnRegister.setOnClickListener {
            val moveRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = true
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text!")
                checkLogin = false
            }
            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text!")
                checkLogin = false
            }

            if(!checkLogin) {
                return@OnClickListener
            }else {
                LoginCheck()
            }

//            if(username == "admin" && password == "admin" || (username == tempUsername && password == tempPass)) {
//                checkLogin = true
//            }


//            val db by lazy{ UserDB(this) }
//            val userDao = db.userDao()

//            val user = userDao.checkUser(username, password)
//            if(user!= null){
//                sharedPreferences.edit()
//                    .putInt("id", user.id)
//                    .apply()
//
//                checkLogin = true
//            }
        })


        btnClear.setOnClickListener{
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty() && password.isEmpty()){
                Snackbar.make(mainLayout, "Field Masih Kosong", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            inputUsername.editText?.setText("")
            inputPassword.editText?.setText("")

            Snackbar.make(mainLayout, "Success Clear Field", Snackbar.LENGTH_LONG).show()
        }

    }

    private fun LoginCheck() {
        //  setLoading(true)

        val userprofil = UserModel(
            0,
            inputUsername.getEditText()?.getText().toString(),
            inputPassword.getEditText()?.getText().toString(),
            "",
            "",
            ""

        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UserApi.CHECK, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, UserModel::class.java)

                if(user!=null) {
                    var checkObj = JSONObject(response.toString())
                    val  check = checkObj.getJSONObject("data")

//                    Toast.makeText(this@MainActivity, "Login berhasil", Toast.LENGTH_SHORT).show()

                    //UI Toast notif
                    MotionToast.darkColorToast(this,
                        "Login Berhasil",
                        "Pemai",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",check.getInt("id"))
                        .putString("pass",check.getString("password"))
                        .putString("tgl",check.getString("tanggalLahir"))
                        .apply()
                    startActivity(intent)
                }else {
//                    Toast.makeText(this@MainActivity, "Login gagal", Toast.LENGTH_SHORT).show()
                    //UI Toast Notification
                    MotionToast.darkColorToast(this,
                        "Login Gagal!",
                        "Pemai",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    return@Listener
                }

            }, Response.ErrorListener { error ->
                // setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MainActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }

            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(userprofil)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
}