package com.documentstorage.app

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.documentstorage.app.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Local())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.local -> replaceFragment(Local())
                R.id.add -> replaceFragment(Add())

                else -> replaceFragment(Local())
            }
            true
        }

        binding.testPdfButton.setOnClickListener() {
            testPDFViewer()
        }

        if (checkPermissions())
            requestPermission()
    }

    private fun checkPermissions(): Boolean {
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun testPDFViewer() {
        val file = generatePDFTest()

        val intent = Intent(this, PDFViewerActivity::class.java)
        intent.putExtra(
            "filePath",
            file.path
        ) // Replace "key" with your desired key and "value" with the actual value to pass
        startActivity(intent)
    }

    private fun generatePDFTest(): File {
        var pdfDocument = PdfDocument()

        var paint = Paint()
        var title = Paint()

        var myPageInfo: PdfDocument.PageInfo? = PdfDocument.PageInfo.Builder(1120, 3000, 1).create()

        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        var canvas: Canvas = myPage.canvas

        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        title.textSize = 15F
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
        canvas.drawText("Geeks for Geeks", 209F, 80F, title)
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        title.textSize = 15F
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)
        pdfDocument.finishPage(myPage)

        val file = File(this.getExternalFilesDir(null), "GFG.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(applicationContext, "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }

        pdfDocument.close()
        return file
    }
}