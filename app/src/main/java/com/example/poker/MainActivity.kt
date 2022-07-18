package com.example.poker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.poker.Data.User

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    lateinit var list: MutableList<User>
    lateinit var ImgView: ImageView
    lateinit var recview: RecyclerView
    lateinit var Develop: TextView
    lateinit var startbutton : Button
    lateinit var viewPager2: ViewPager2
    lateinit var SliderAdapter: SliderAdapter
    private val SliderItems = mutableListOf<SliderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager2 = findViewById(R.id.viewPagerImageSlider)
        SliderItems.add(SliderItem(R.drawable.poker, 1))
        SliderItems.add(SliderItem(R.drawable.poker,2))
        SliderItems.add(SliderItem(R.drawable.poker,3))
        SliderItems.add(SliderItem(R.drawable.poker,4))
        SliderAdapter = SliderAdapter(SliderItems)
        viewPager2.adapter = SliderAdapter
        SliderAdapter.itemClick = object : SliderAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                if(SliderItems[position].num == 1){
                    val intent= Intent(this@MainActivity, PokerActivity::class.java)
                    startActivity(intent)
                }
                else if(SliderItems[position].num == 2){
                    val intent= Intent(this@MainActivity, SlotActivity::class.java)
                    startActivity(intent)
                }
                else if(SliderItems[position].num == 3){
                    val intent= Intent(this@MainActivity, RouletteActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        viewPager2.clipToPadding= false
        viewPager2.clipChildren = false
        viewPager2.offscreenPageLimit = 3
        viewPager2.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(36))
        compositePageTransformer.addTransformer(object : ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85f + r *  0.15f
            }
        })
        SliderAdapter.notifyDataSetChanged()
        viewPager2.setPageTransformer(compositePageTransformer)
        startbutton = findViewById(R.id.startbutton)
//        startbutton.setOnClickListener{
//            val intent = Intent(this@MainActivity, LobbyActivity::class.java)
//            startActivity(intent)
//        }
//        if (Global.currentPlayerName != null){
//            startbutton.visibility =  View.VISIBLE
//        }
//        else{
//            startbutton.visibility =  View.GONE
//        }
    }

}