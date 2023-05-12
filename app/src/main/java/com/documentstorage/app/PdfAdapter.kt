package com.documentstorage.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PdfAdapter(var lista: List<PdfData>): RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.logoIv)
        val titleTv: TextView = itemView.findViewById(R.id.titleTv)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(lista: List<PdfData>) {
        this.lista = lista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return PdfViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.lista.size
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.logo.setImageResource(lista[position].logo)
        holder.titleTv.text = lista[position].title
    }

}