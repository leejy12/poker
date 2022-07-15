package com.example.poker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TableAdapter(private val itemList: ArrayList<Table>) : RecyclerView.Adapter<TableAdapter.Holder>() {
    interface OnItemClickListener {
        fun onCardViewClick(view: View, table: Table, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_table, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: TableAdapter.Holder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val tableName = itemView?.findViewById<TextView>(R.id.tableName)
        private val maxPlayers = itemView?.findViewById<TextView>(R.id.maxpeople)
        private val curPlayers = itemView?.findViewById<TextView>(R.id.currentpeople)
        private val cardView = itemView?.findViewById<CardView>(R.id.tableCardView)

        fun bind(table: Table) {
            tableName?.text = table.tableName
            maxPlayers?.text = table.maxPlayers.toString()
            curPlayers?.text = table.currentPlayers.toString()

            cardView?.setOnClickListener {
                listener?.onCardViewClick(itemView, table, adapterPosition)
            }
        }
    }
}
