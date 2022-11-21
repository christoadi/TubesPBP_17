package com.example.ugd1

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd1.api.UserApi
import com.example.ugd1.databinding.ActivityEditProfileBinding
import com.example.ugd1.model.UserModel
import com.example.ugd1.room.User
import com.example.ugd1.room.UserDB
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditProfileFragment : Fragment() {
    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ActivityEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queue = Volley.newRequestQueue(activity)
        sharedPreferences = (activity as HomeActivity).getSharedPreferences("login", Context.MODE_PRIVATE)

        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        var id = sharedPreferences.getInt("id", 0)
        var password = sharedPreferences.getString("pass", null)
        var tanggalLahir = sharedPreferences.getString("tgl", null)

        var check = true

        binding.btnUpdate.setOnClickListener {
            if (binding.tietEmail.text.toString().isEmpty()){
                binding.tietEmail.setError("Kosong")
                check =false
            }
            if(binding.tietNoTelp.text.toString().isEmpty()) {
                binding.tietNoTelp.setError("Kosong")
                check =false
            }
            if(binding.tietUsername.text.toString().isEmpty()) {
                binding.tietUsername.setError("Kosong")
                check =false
            }
            if(!check) {
                check = true
                return@setOnClickListener
            }else {
                if (password != null) {
                    if (tanggalLahir != null) {
                        updateProfile(id, password, tanggalLahir)
                    }
                }
                transitionFragment(ProfilFragment())
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateData() {
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()

        val id = sharedPreferences.getInt("id", 0)

        val getUser = userDao.getUser(id)

        val user = User(id,
            binding.tietUsername.text.toString(),
            getUser.password,
            binding.tietEmail.text.toString(),
            getUser.tanggalLahir,
            binding.tietNoTelp.text.toString()
        )
        userDao.updateUser(user)
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.fragment_profil, fragment)
            .addToBackStack(null).commit()
        transition.hide(EditProfileFragment())
    }

    private fun updateProfile(id: Int, password: String, tanggalLahir: String) {
//        setLoading(true)

        val user = UserModel(
            id,
            binding.tietUsername.text.toString(),
            password,
            binding.tietEmail.text.toString(),
            tanggalLahir,
            binding.tietNoTelp.text.toString(),
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.PUT, UserApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, UserModel::class.java)

                if(user != null) {
                    var userObject = JSONObject(response.toString())
                    val userData = userObject.getJSONObject("data")

                    sharedPreferences.edit()
                        .putInt("id",userData.getInt("id"))
                        .putString("pass",userData.getString("password"))
                        .apply()
                    Toast.makeText(activity, "User Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                }
//                setLoading(false)

            }, Response.ErrorListener { error ->
//                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        activity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)

    }
}