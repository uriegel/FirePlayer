package de.uriegel.fireplayer

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class MainActivity : ActivityEx(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    @Serializable
    data class Files(val files: Array<String>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videos.layoutManager = GridLayoutManager(this, 6)
        videos.setHasFixedSize(true)

        launch {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
            var url = preferences.getString("url", "")
            if (url!!.length < 6) {
                activityRequest(Intent(this@MainActivity, SettingsActivity::class.java))
                url = preferences.getString("url", "")
            }
            MainActivity.url = url!!

            fun onItemClick(film: String) {
                val intent = Intent(this@MainActivity, PlayerActivity::class.java)
                intent.putExtra("film", film)
                startActivity(intent)
            }

            try {
                val result = httpGet("${MainActivity.url}/video/list")
                val files = Json.decodeFromString<Files>(result)
                    .files
                    .filter { it.length > 4 }
                    .map { it.substring(0, it.length - 4) }
                videos.adapter = VideosAdapter(files.toTypedArray(), ::onItemClick)
            } catch (e: Exception) { }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        lateinit var url: String
    }
}
