package com.diplomado.galleryapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.diplomado.galleryapp.model.Image
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ImageAdapter
    private lateinit var queue: RequestQueue
    private val listImage = mutableListOf<Image>()
    private var count:Int = 1

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.topAppBar)
        val themeSwitch:Switch = findViewById(R.id.switchTheme)
        val refresh:Button = findViewById(R.id.refreshBtn)
        setSupportActionBar(toolbar)
        /*toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.more -> {
                    getData()
                    true
                }
                else -> false
            }
        }*/
        val gallery: RecyclerView = findViewById(R.id.galleryRecyclerView)
        adapter = ImageAdapter(listImage, onItemClicked = {
            val intent = Intent(this, FullImageActivity::class.java)
            intent.putExtra("IMAGE_URL", it.download_url )
            intent.putExtra("AUTHOR", it.author )
            intent.putExtra("WIDTH", it.width )
            intent.putExtra("HEIGHT", it.height )
            startActivity(intent)
        })
        /*val listImage = mutableListOf(
            Image(
                "Title 1",
                "https://images.unsplash.com/photo-1673851333040-e1f6c8857a51?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80",
                "Description 1",
                "01/11/2001"
            ),
            Image(
                "Title 2",
                "https://images.unsplash.com/photo-1598714673521-98d539a175f4?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8cGxhY2VzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
                "Description 2",
                "14/09/2003"
            ),
            Image(
                "Title 3",
                "https://images.unsplash.com/photo-1615971304240-82c1793aa182?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTZ8fHBsYWNlc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "Description 3",
                "24/06/2005"
            ),
            Image(
                "Title 4",
                "https://images.unsplash.com/photo-1573532271406-b13cb961e22e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzV8fHBsYWNlc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "Description 4",
                "08/12/2010"
            ),
            Image(
                "Title 4",
                "https://images.unsplash.com/photo-1573532271406-b13cb961e22e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzV8fHBsYWNlc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "Description 4",
                "08/12/2010"
            ),
            Image(
                "Title 4",
                "https://images.unsplash.com/photo-1573532271406-b13cb961e22e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzV8fHBsYWNlc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "Description 4",
                "08/12/2010"
            ),
            Image(
                "Title 4",
                "https://images.unsplash.com/photo-1573532271406-b13cb961e22e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzV8fHBsYWNlc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "Description 4",
                "08/12/2010"
            ),
            Image(
                "Title 4",
                "https://images.unsplash.com/photo-1573532271406-b13cb961e22e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzV8fHBsYWNlc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60",
                "Description 4",
                "08/12/2010"
            )
        )*/

        gallery.layoutManager = GridLayoutManager(this, 2)
        gallery.adapter = adapter
        queue = Volley.newRequestQueue(this)

        refresh.setOnClickListener { getData() }
        //getData()
        /*
        image1.setOnClickListener {
            val intent = Intent(this, FullImageActivity::class.java)
            intent.putExtra("IMAGE_URL", url)
            startActivity(intent)
        }
        */
        themeSwitch.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.menu, menu)
        return true
    }


    private fun getData() {
        val url = "https://picsum.photos/v2/list?page=$count&limit=14"
        val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    adapter.clear()
                    adapter.notifyDataSetChanged()
                    for (i in 0 until response.length()) {
                        parseImage(response.getJSONObject(i))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show()
                error.printStackTrace()
            }
        )
        queue.add(jsonRequest)
        count++
    }

    private fun parseImage(response: JSONObject) {

        val image = Image(
            response.getString("id"),
            response.getString("author"),
            response.getDouble("width"),
            response.getDouble("height"),
            response.getString("url"),
            response.getString("download_url")
        )

        listImage.add(image)
    }

}