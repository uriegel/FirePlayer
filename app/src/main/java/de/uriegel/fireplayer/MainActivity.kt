package de.uriegel.fireplayer

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import de.uriegel.activityextensions.ActivityRequest
import de.uriegel.activityextensions.http.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    @Serializable
    data class Files(val files: Array<String>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun isTV(): Boolean { return android.os.Build.MODEL.contains("AFT") }
        if (isTV())
            setTheme(R.style.FirePlayerTheme)

        setContentView(R.layout.activity_main)

        videos.layoutManager = GridLayoutManager(this, 6)
        videos.setHasFixedSize(true)

        launch {
            listItems()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            showSettings()
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.menu_settings) {
            showSettings()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSettings() {
        launch {
            activityRequest.launch(Intent(this@MainActivity, SettingsActivity::class.java))
            listItems()
        }
    }

    private suspend fun listItems() {
        try {
            fun onItemClick(film: String) {
                val intent = Intent(this@MainActivity, PlayerActivity::class.java)
                intent.putExtra("film", film)
                startActivity(intent)
            }

            videos.adapter = VideosAdapter(emptyArray(), ::onItemClick)

            val preferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
            var url = preferences.getString("url", "")
            if (url!!.length < 6) {
                activityRequest.launch(Intent(this@MainActivity, SettingsActivity::class.java))
                url = preferences.getString("url", "")
            }

            basicAuthentication(preferences.getString("name", "")!!, preferences.getString("auth_pw", "")!!)
            MainActivity.url = url!!

            val result = getString("${MainActivity.url}/video/list")
            val files = Json.decodeFromString<Files>(result)
                .files
                .filter { it.length > 4 }
                .map { it.substring(0, it.length - 4) }
            videos.adapter = VideosAdapter(files.toTypedArray(), ::onItemClick)
        } catch (e: Exception) {
            val es = e
            Toast.makeText(this@MainActivity, "Die Einstellungen für \"URL\" oder \"Anmeldung\" sind fehlerhaft", Toast.LENGTH_LONG).show()
        }
    }

    private val activityRequest = ActivityRequest(this)

    companion object {
        lateinit var url: String
    }
}
