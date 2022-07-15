package com.example.poker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poker.Data.User

class RecyclerViewAdapter(val dataset: MutableList<User>, val listener: RecyclerViewClickListener): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_table,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int){
        holder.bind(dataset[position], position)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val displayname : TextView = itemView.findViewById(R.id.tableName)
        private val currentpeople: TextView = itemView.findViewById(R.id.currentpeople)
        private val maxpeople: TextView = itemView.findViewById(R.id.maxpeople)
        private val c_view: View = view
        fun bind(item: User, position: Int) {
            displayname.text = item.displayName
            c_view.setOnClickListener{
                listener.onClick(c_view, position)
            }

//            val pos = adapterPosition
//            if(pos!= RecyclerView.NO_POSITION)
//            {
//                itemView.setOnClickListener {
//                    listener?.onItemClick(itemView,item,pos)
//                }
//            }
        }
    }



    override fun getItemCount(): Int = dataset.size
}