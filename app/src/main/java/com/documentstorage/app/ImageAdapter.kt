package com.documentstorage.app

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(private val lista: List<ImageData>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var desiredWidth: Int = 0
    private val verticalAspectRatio: Float = 0.75f

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        val tvImageNumber: TextView = itemView.findViewById(R.id.tvImageNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        val screenWidth = parent.context.resources.displayMetrics.widthPixels
        desiredWidth = screenWidth / 2
        val layoutParams = view.layoutParams
        layoutParams.width = desiredWidth
        view.layoutParams = layoutParams
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageData = lista[position]
        holder.ivImage.setImageBitmap(imageData.image)
        holder.ivImage.scaleType = ImageView.ScaleType.CENTER_CROP // Maintain aspect ratio

        val layoutParams = holder.ivImage.layoutParams
        val height = (desiredWidth.toFloat() / verticalAspectRatio)
        layoutParams.height = height.toInt()
        holder.ivImage.layoutParams = layoutParams
        val pos = position + 1
        holder.tvImageNumber.text = "$pos)"
    }

}
