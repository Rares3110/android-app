package com.documentstorage.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import java.io.File

class PDFViewerActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)

        pdfView = findViewById(R.id.pdfView)
        val path = intent.getStringExtra("filePath")
        val file = File(path)
        displayPDF(file)
    }

    private fun displayPDF(pdfFile: File) {
        pdfView.fromFile(pdfFile)
            .defaultPage(0)
            .scrollHandle(DefaultScrollHandle(this))
            .load()
    }
}