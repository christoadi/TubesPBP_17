package com.example.ugd1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd1.api.UserApi
import com.example.ugd1.databinding.FragmentProfilBinding
import com.example.ugd1.room.UserDB
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class ProfilFragment : Fragment() {

    private var _binding  : FragmentProfilBinding?= null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queue = Volley.newRequestQueue(activity)
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        var id = sharedPreferences.getInt("id", 0)
        showUser(id)

        binding.btnUpdate.setOnClickListener {
            transitionFragment(EditProfileFragment())
        }

        binding.btnQr.setOnClickListener(View.OnClickListener {
            val moveQr = Intent(this@ProfilFragment.context, QrScan::class.java)
            startActivity(moveQr)
        })

        binding.btnLogout.setOnClickListener {
            val moveLogout= Intent(this@ProfilFragment.context, MainActivity::class.java)
            startActivity(moveLogout)
        }

//        val camera: ImageView = view.findViewById(R.id.homeHospital)
        binding.homeHospital.setOnClickListener {
            val moveCamera = Intent(this@ProfilFragment.context, CameraActivity::class.java)
            startActivity(moveCamera)
        }
    }

    private fun setProfil() {
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()

        val user = userDao.getUser(sharedPreferences.getInt("id", 0))
        binding.viewUsername.setText(user.username)
        binding.viewEmail.setText(user.email)
        binding.viewNomorTelepon.setText(user.nomorTelepon)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.fragment_profil, fragment)
            .addToBackStack(null).commit()
        transition.hide(ProfilFragment())
    }

    private fun showUser(id: Int) {
//        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, UserApi.GET_BY_ID_URL + id, Response.Listener { response ->
                // val gson = Gson()
                // val mahasiswa = gson.fromJson(response, Mahasiswa::class.java)

                var userObject = JSONObject(response.toString())
                val userData = userObject.getJSONObject("data")

                binding.viewUsername.setText(userData.getString("username"))
                binding.viewEmail.setText(userData.getString("email"))
                binding.viewNomorTelepon.setText(userData.getString("nomorTelepon"))

                Toast.makeText(activity, "Data User berhasil diambil!", Toast.LENGTH_SHORT).show()
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
        }
        queue!!.add(stringRequest)
    }

}