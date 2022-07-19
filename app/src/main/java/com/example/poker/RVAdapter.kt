package com.example.poker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RVAdapter(val context : Context, var List : MutableList<RankContents>): RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)

        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: RVAdapter.ViewHolder, position: Int) {
        holder.bindItems(List[position])
    }

    override fun getItemCount(): Int {
        return List.size
    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item : RankContents){
            val rv_img = itemView.findViewById<ImageView>(R.id.rvImageArea)
            val rv_text = itemView.findViewById<TextView>(R.id.rvTextArea)
            rv_text.text= item.title
            Glide.with(context)
                .load(item.Image)
                .into(rv_img)

        }
    }
}