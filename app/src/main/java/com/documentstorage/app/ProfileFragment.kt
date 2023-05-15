package com.documentstorage.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var logoutButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logoutButton = view.findViewById(R.id.logoutButton)
        firebaseAuth = FirebaseAuth.getInstance()

        val user = firebaseAuth.currentUser
        emailTextView = view.findViewById(R.id.emailText)
        emailTextView.text = user?.email

        val ivProfilePicture = view.findViewById<ImageView>(R.id.ivProfilePicture)
//        Picasso.get().load("").into(ivProfilePicture)  // pentru Firebase
        val profileIcon = ContextCompat.getDrawable(requireContext(), R.drawable.avatar)
        ivProfilePicture.setImageDrawable(profileIcon)

        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}