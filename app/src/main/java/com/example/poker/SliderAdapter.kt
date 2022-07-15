package com.example.poker

import com.example.poker.SliderItem
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView
import com.example.poker.SliderAdapter.SliderViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.poker.R
import com.makeramen.roundedimageview.RoundedImageView

class SliderAdapter(private val sliderItems: List<SliderItem>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<SliderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position])
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: RoundedImageView

        fun setImage(sliderItem: SliderItem) {
            imageView.setImageResource(sliderItem.image)
        }

        init {
            imageView = itemView.findViewById(R.id.imageSlide)
        }
    }
}