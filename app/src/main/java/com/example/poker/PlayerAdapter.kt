package com.example.poker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(private val itemList: ArrayList<Player>) : RecyclerView.Adapter<PlayerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_player, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: PlayerAdapter.Holder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val playerName = itemView?.findViewById<TextView>(R.id.playerName)
        private val playerChipCount = itemView?.findViewById<TextView>(R.id.playerChipCount)

        fun bind(player: Player) {
            playerName!!.text = player.name
            playerChipCount!!.text = player.chipCount.toString()
        }
    }
}
