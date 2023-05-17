package com.documentstorage.app

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import com.documentstorage.app.databinding.ActivityMainBinding
import com.documentstorage.app.databinding.ActivityPdfviewerBinding
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream

class PDFViewerActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var binding: ActivityPdfviewerBinding
    private lateinit var file: File
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private lateinit var name: String
    private lateinit var type: FileType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfviewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            btnBackHandler()
        }

        binding.btnUpload.setOnClickListener {
            sendFileToCloud()
        }

        binding.btnSave.setOnClickListener {
            saveFile()
        }

        binding.btnDelete.setOnClickListener {
            deleteFile()
        }

        binding.btnShare.setOnClickListener {
            share()
        }

        pdfView = findViewById(R.id.pdfView)
        name = intent.getStringExtra("name")!!
        type = enumValueOf<FileType>(intent.getStringExtra("type")!!)

        if(type == FileType.Files) {
            binding.btnSave.visibility = View.GONE
            file = File(getExternalFilesDir(null)?.path + "/" + name)
            displayPDF(file)
        } else {
            binding.btnUpload.visibility = View.GONE
            binding.btnShare.visibility = View.GONE
            val user = auth.currentUser
            val fileRef = storage.reference.child(user?.uid!! + "/" + name)

            file = File.createTempFile("temp", ".pdf")
            fileRef.getFile(file)
                .addOnSuccessListener {
                    displayPDF(file)
                    Log.d("FirebaseStorage", "File downloaded successfully: ${file.absolutePath}")
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseStorage", "Error downloading file: ${exception.message}")
                    finish()
                }
        }
    }

    private fun sendFileToCloud() {
        val user = auth.currentUser
        val fileUri: Uri = Uri.fromFile(file)

        Log.i("firebase", user?.uid!! + "/" + file.name)

        val storageRef: StorageReference = storage.reference.child(
            user?.uid!! + "/" + file.name
        )
        val uploadTask = storageRef.putFile(fileUri)

        uploadTask.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
            // Update progress UI
        }.addOnSuccessListener {
            Log.i("firebase", "file uploaded")
            Toast.makeText(applicationContext, "File uploaded on cloud", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { exception ->
            exception.fillInStackTrace()
        }
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

    private fun saveFile() {
        saveFileToDirectory(file, getExternalFilesDir(null)!!)
    }

    private fun deleteFile() {
        if(type == FileType.Files) {
            val isDeleted = file.delete()
            if (isDeleted) {
                // File deleted successfully
                finish()
            } else {
                // Unable to delete the file
                Toast.makeText(applicationContext, "Deletion failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            val user = auth.currentUser
            val storageRef: StorageReference = storage.getReference(user?.uid!! + "/" + name)
            storageRef.delete()
                .addOnSuccessListener {
                    // File deleted successfully
                    finish()
                }
                .addOnFailureListener { exception ->
                    // Error occurred while deleting the file
                    Toast.makeText(applicationContext, "Deletion failed", Toast.LENGTH_SHORT).show()
                    exception.printStackTrace()
                }
        }
    }

    private fun saveFileToDirectory(sourceFile: File, targetDirectory: File) {
        val fileName = name
        val targetFile = File(targetDirectory, fileName)

        try {
            val inputStream = sourceFile.inputStream()
            val outputStream = FileOutputStream(targetFile)
            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            // File saved successfully
            Toast.makeText(applicationContext, "File saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Handle any errors that occurred during the file saving process
            e.printStackTrace()
        }
    }

    private fun share() {
        val uri = FileProvider.getUriForFile(this, "${this.packageName}.provider", file)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/pdf"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val chooserIntent = Intent.createChooser(shareIntent, "Share to:")
        if (chooserIntent.resolveActivity(this.packageManager) != null) {
            this.startActivity(chooserIntent)
        }
    }
}