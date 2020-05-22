package de.uriegel.fireplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class MainActivity : AppCompatActivity() {

    @Serializable
    data class Files(val files: Array<String>)

    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videos.layoutManager = GridLayoutManager(this, 6)
        videos.setHasFixedSize(true)

        fun onItemClick(film: String) {
            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
            intent.putExtra("film", film)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val result = httpGet("https://uriegel.de/videos")
            val json = Json(JsonConfiguration.Stable)
            val files = json.parse<Files>(result).files.map { it.substring(0, it.length - 4) }
            videos.adapter = VideosAdapter(files.toTypedArray(), ::onItemClick)
        }
    }
}
