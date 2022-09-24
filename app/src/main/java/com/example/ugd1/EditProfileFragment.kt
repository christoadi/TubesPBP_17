package com.example.ugd1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ugd1.databinding.ActivityEditProfileBinding
import com.example.ugd1.room.User
import com.example.ugd1.room.UserDB

class EditProfileFragment : Fragment() {
    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding!!

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
                updateData()
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

}