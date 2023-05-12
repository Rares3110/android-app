package com.documentstorage.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.documentstorage.app.databinding.ActivityMainBinding
import com.documentstorage.app.databinding.ActivityPdfviewerBinding
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import java.io.File

class PDFViewerActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var binding : ActivityPdfviewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfviewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            btnBackHandler()
        }

        pdfView = findViewById(R.id.pdfView)
        val path = intent.getStringExtra("filePath")
        val file = File(path)
        Log.i("log", file.path)
        displayPDF(file)
    }

    private fun btnBackHandler() {
        finish()
    }

    private fun displayPDF(pdfFile: File) {
        pdfView.fromFile(pdfFile)
            .defaultPage(0)
            .scrollHandle(DefaultScrollHandle(this))
            .load()
    }
}