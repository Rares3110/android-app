package com.documentstorage.app

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class AddFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private var imageList = ArrayList<ImageData>()
    private lateinit var adapter: ImageAdapter
    private lateinit var nameOfDoc: EditText
    private lateinit var generateButton: MaterialButton

    private val REQUEST_CODE_CAPTURE = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvImageList)
        layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        adapter = ImageAdapter(imageList)
        recyclerView.adapter = adapter
        val captureButton: TextView = view.findViewById(R.id.btnCamera)
        captureButton.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CAPTURE)
        }
        generateButton = view.findViewById(R.id.btnGeneratePDF)
        nameOfDoc = view.findViewById(R.id.etName)
        generateButton.setOnClickListener {
            val pdfName = nameOfDoc.text.toString().trim()
            val generatedName: String = pdfName.ifEmpty {
                val dateFormat = SimpleDateFormat("dd_MM_yyyy HH_mm_ss", Locale.getDefault())
                val currentDate = dateFormat.format(Date())
                "PDF $currentDate"
            }
            generatePdf(generatedName)
        }
        val deleteButton: MaterialButton = view.findViewById(R.id.btnDelete)
        deleteButton.setOnClickListener{
            imageList.clear()
            nameOfDoc.setText("")
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imagePath = data?.getStringExtra("imagePath")
            if (imagePath != null) {
                val bitmap = BitmapFactory.decodeFile(imagePath)
                // Rotate the bitmap by 90 degrees
                val rotatedBitmap = rotateBitmap(bitmap, 90f)
                imageList.add(ImageData(rotatedBitmap, imagePath))
                adapter.notifyItemInserted(imageList.size - 1)
            }
        }
    }

    private fun generateButtonAnimation(success: Boolean) {
        val originalColor = Color.parseColor("#018786")
        var targetColor: Int

        if(success) {
            generateButton.text = "SAVED"
            targetColor = Color.parseColor("#3700B3")
            generateButton.setBackgroundColor(targetColor)
        }
        else {
            generateButton.text = "NO PICTURES"
            targetColor = Color.parseColor("#DC2626")
            generateButton.setBackgroundColor(targetColor)
        }


        val colorAnimator = ObjectAnimator.ofObject(
            generateButton,
            "backgroundColor",
            ArgbEvaluator(),
            targetColor,
            originalColor
        )

        Handler(Looper.getMainLooper()).postDelayed({
            generateButton.text = "GENERATE PDF"
        }, 1000)

        colorAnimator.startDelay = 1000
        colorAnimator.duration = 1500
        colorAnimator.start()
    }

    private fun generatePdf(name: String) {
        val document = Document()
        val outputDir = requireContext().getExternalFilesDir(null)

        try {
            if (imageList.size == 0) {
                throw Exception("You cannot create empty PDFs")
            }
            val pdfFileName = "$name.pdf"
            val pdfFile = File(outputDir, pdfFileName)
            PdfWriter.getInstance(document, FileOutputStream(pdfFile))
            document.open()

            for (imageData in imageList) {
                val image = Image.getInstance(imageData.path)
                image.setRotationDegrees(-90f)
                image.scaleToFit(PageSize.A4.width, PageSize.A4.height)
                document.add(image)
                document.newPage()
            }
            //showToast("PDF created successfully")
            generateButtonAnimation(true)
            nameOfDoc.setText("")
            imageList.clear()
            adapter.notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
            //showToast("Failed to create PDF")
            generateButtonAnimation(false)
        } finally {
            document.close()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }
}
