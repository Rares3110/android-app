package com.documentstorage.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class CloudFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var pdfList: ArrayList<PdfData>
    private lateinit var adapter: PdfAdapter

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

        addDataToList()
        adapter = PdfAdapter(pdfList)
        recyclerView.adapter = adapter

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

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList  =  ArrayList<PdfData>()
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
    private fun addDataToList() {
        pdfList = ArrayList<PdfData>()
        pdfList.add(PdfData("pdf1", R.drawable.baseline_cloud_24))
        pdfList.add(PdfData("pdf2", R.drawable.baseline_cloud_24))
        pdfList.add(PdfData("pdf3", R.drawable.baseline_cloud_24))
        pdfList.add(PdfData("pdf4", R.drawable.baseline_cloud_24))
    }
}