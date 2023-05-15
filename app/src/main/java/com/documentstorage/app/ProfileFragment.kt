package com.documentstorage.app

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.TextView
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

        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}