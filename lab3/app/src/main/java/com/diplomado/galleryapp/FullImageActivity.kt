package com.diplomado.galleryapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class FullImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        val fullImage:ImageView = findViewById(R.id.fullImageView)
        val author:TextView = findViewById(R.id.author)
        val width:TextView = findViewById(R.id.width)
        val height:TextView = findViewById(R.id.height)

        val bundle = intent.extras

        title = bundle?.getString("AUTHOR").toString()
        Glide.with(this).load(bundle?.getString("IMAGE_URL")).into(fullImage)
        author.text = bundle?.getString("AUTHOR").toString()
        width.text = bundle?.getDouble("WIDTH").toString()
        height.text = bundle?.getDouble("HEIGHT").toString()
    }
}