package com.documentstorage.app

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PDFAdapter(private var lista: List<PDFData>) : RecyclerView.Adapter<PDFAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener { // Adauga interfata View.OnClickListener pentru a face elementul clickable
        // Clasa pentru elementul din lista
        init {
            itemView.setOnClickListener(this) // Seteaza listenerul pe itemView
        }

        val logo: ImageView = itemView.findViewById(R.id.ivLogo)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        override fun onClick(view: View?) {
            // Implementeaza metoda onClick din View.OnClickListener
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Executa actiunea corespunzatoare cand se face click pe item-ul respectiv
                Log.i("log", "Ai apasat pe PDF")
                val intent = Intent(itemView.context, PDFViewerActivity::class.java)
                intent.putExtra(
                    "name",
                    tvTitle.text
                ) // Replace "key" with your desired key and "value" with the actual value to pass
                intent.putExtra(
                    "type",
                    lista.find{ it.title == tvTitle.text }?.type?.name
                )
                itemView.context.startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(lista: List<PDFData>) {
        this.lista = lista
        notifyDataSetChanged() // Notifica adapterul despre modificarea listei de itemi
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_item, parent, false) // Creeaza o instanta a layout-ului list_item pentru fiecare item
        return PdfViewHolder(view) // Returneaza un ViewHolder care contine referintele catre elementele grafice din layout-ul list_item
    }

    override fun getItemCount(): Int {
        return this.lista.size
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.logo.setImageResource(lista[position].logo) // Seteaza imaginea in ImageView pentru item-ul corespunzator
        holder.tvTitle.text = lista[position].title // Seteaza textul in TextView pentru item-ul corespunzator
        holder.tvDate.text = lista[position].date
    }

}
