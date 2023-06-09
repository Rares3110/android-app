package com.documentstorage.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CloudFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var pdfList =  ArrayList<PDFData>()
    private var adapter = PDFAdapter(pdfList)
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        searchView = view.findViewById(R.id.searchView)
        // Use recyclerView and searchView here
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Scoatem focusul de pe tastatura
        view?.post {
            searchView.clearFocus()
        }

        addData()
    }

    private fun filterList(query: String?) {
        if (!query.isNullOrEmpty()) {
            val filteredList  =  ArrayList<PDFData>()
            for (it in pdfList) {
                if (it.title.lowercase(Locale.ROOT).contains(query.lowercase()))
                    filteredList.add(it)
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                adapter.setFilteredList(filteredList)
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addData() {
        val user = auth.currentUser
        val folderRef = storage.reference.child(user?.uid!! + "/")
        pdfList.clear()

        folderRef.listAll()
            .addOnSuccessListener { result ->
                for(item in result.items) {
                    Log.i("firebase", item.name)
                    pdfList.add(PDFData(item.name, R.drawable.baseline_blue_cloud_24, "", FileType.Cloud))
                }

                adapter = PDFAdapter(pdfList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                exception.fillInStackTrace()
            }
    }
}