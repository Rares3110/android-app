package com.documentstorage.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(private val lista: List<ImageData>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var screenWidth: Int = 0

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        val tvImageNumber: TextView = itemView.findViewById(R.id.tvImageNumber)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        screenWidth = parent.context.resources.displayMetrics.widthPixels
        return ImageViewHolder(view)
    }


    override fun getItemCount(): Int {
        return this.lista.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.ivImage.setImageResource(lista[position].image)
        val layoutParams = holder.ivImage.layoutParams
        layoutParams.width = screenWidth / 2
        holder.ivImage.layoutParams = layoutParams
        val pos = position + 1
        holder.tvImageNumber.text = "$pos)"
    }


}
