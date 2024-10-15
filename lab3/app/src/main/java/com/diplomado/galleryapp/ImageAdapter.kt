package com.diplomado.galleryapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diplomado.galleryapp.model.Image

class ImageAdapter(
    private val imageList: MutableList<Image>,
    private val onItemClicked: (Image) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.image_item_layout, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = imageList[position]
        holder.textView.text = item.author
        Glide.with(holder.imageView.context).load(item.download_url)
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(holder.imageView)
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun clear() {
        val size: Int = imageList.size
        imageList.clear()
        notifyItemRangeRemoved(0, size)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val textView: TextView

        init {
            imageView = view.findViewById(R.id.pictureImageView)
            textView = view.findViewById(R.id.pictureTextView)
        }

    }

}