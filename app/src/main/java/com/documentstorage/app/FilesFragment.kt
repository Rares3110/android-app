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
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FilesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var pdfList =  ArrayList<PDFData>()
    private var adapter = PDFAdapter(pdfList)

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
        // Aici actualizam datele
        super.onResume()
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

    private fun addData() {
        pdfList.clear()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        val files = mutableListOf<File>()
        val directory = requireContext().getExternalFilesDir(null)
        if(directory != null)
            searchFilesWithType(directory, files)

        files.sortByDescending { it.lastModified() } // Sort the files based on last modified date in descending order

        for(file in files) {
            val date = Date(file.lastModified())
            pdfList.add(PDFData(file.name, R.drawable.pdf_ico, dateFormat.format(date), FileType.Files))
        }
        adapter = PDFAdapter(pdfList)
        recyclerView.adapter = adapter
    }

    private fun searchFilesWithType(directory: File, fileList: MutableList<File>) {
        val files = directory.listFiles()

        if (files != null) {
            for (file in files) {
                if (!file.isDirectory) {
                    if (file.name.endsWith(".pdf", ignoreCase = true)) {
                        fileList.add(file)
                    }
                }
            }
        }
    }
}