package com.documentstorage.app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var imageList: ArrayList<ImageData>
    private lateinit var adapter: ImageAdapter

    private val REQUEST_CODE_CAPTURE = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvImageList)
        layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        imageList = ArrayList()

        adapter = ImageAdapter(imageList)
        recyclerView.adapter = adapter
        val captureButton:TextView = view.findViewById(R.id.btnCamera)
        captureButton.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageData = data?.getStringExtra("imageData")
            if (imageData != null) {
                val byteArray = Base64.decode(imageData, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                imageList.add(ImageData(bitmap))
                adapter.notifyItemInserted(imageList.size - 1)
            }
        }
    }

    private fun addImageToRecyclerView(imageData: ImageData) {
        imageList.add(imageData)
        adapter.notifyItemInserted(imageList.size - 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }
}
