package de.uriegel.fireplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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

        lifecycleScope.launch {
            val result = httpGet("https://uriegel.de/videos")
            val json = Json(JsonConfiguration.Stable)
            val files = json.parse<Files>(result).files.map { it.substring(0, it.length - 4) }
            val affe = files
        }

        starter.setOnClickListener {
            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
            startActivity(intent)
        }
    }
}
