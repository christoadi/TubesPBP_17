package com.example.ugd1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ugd1.databinding.FragmentProfilBinding
import com.example.ugd1.room.UserDB


class ProfilFragment : Fragment() {

    private var _binding  : FragmentProfilBinding?= null
    private val binding get() = _binding!!

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
        setProfil()

        binding.btnUpdate.setOnClickListener {
            transitionFragment(EditProfileFragment())
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

}