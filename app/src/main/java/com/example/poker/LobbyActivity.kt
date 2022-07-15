package com.example.poker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class Table(val tableId: String, val tableName: String, val maxPlayers: Int, val currentPlayers: Int)

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

class LobbyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tables: ArrayList<Table>
    private lateinit var tableAdapter: TableAdapter

    private fun getTables() {
        val queue = Volley.newRequestQueue(this)

        val url = "${IP.getIP()}/game/tables"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                for (i in 0 until response.length()) {
                    val tableObj = response.getJSONObject(i)
                    val tableId = tableObj.getString("tableId")
                    val tableName = tableObj.getString("tableName")
                    val maxPlayers = tableObj.getInt("maxPlayers")
                    val currentPlayers = tableObj.getInt("currentPlayers")
                    tables.add(Table(tableId, tableName, maxPlayers, currentPlayers))
                }

                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                tableAdapter = TableAdapter(tables)
                tableAdapter.setOnItemClickListener(object:
                    TableAdapter.OnItemClickListener {
                        override fun onCardViewClick(view: View, table: Table, pos: Int) {
                            println(table.tableId)
                        }
                    }
                )
                recyclerView.adapter = tableAdapter
                tableAdapter.notifyDataSetChanged()
            },
            { error -> Log.d("why?", error.message.toString())}
        )

        jsonArrayRequest.setShouldCache(false)
        queue.add(jsonArrayRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        recyclerView = findViewById(R.id.tablesRecyclerView)
        tables = ArrayList()
        getTables()
    }
}