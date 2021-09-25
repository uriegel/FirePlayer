package de.uriegel.fireplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import de.uriegel.activityextensions.ActivityRequest
import de.uriegel.activityextensions.http.*
import de.uriegel.fireplayer.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@ExperimentalSerializationApi
class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    @Serializable
    data class Files(val files: List<String>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun isTV(): Boolean { return android.os.Build.MODEL.contains("AFT") }
        if (isTV())
            setTheme(R.style.FirePlayerTheme)

        binding.videos.layoutManager = GridLayoutManager(this, 6)
        binding.videos.setHasFixedSize(true)

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

            binding.videos.adapter = VideosAdapter(emptyArray(), ::onItemClick)

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
            binding.videos.adapter = VideosAdapter(files.toTypedArray(), ::onItemClick)
        } catch (e: Exception) {
            Log.w("FP", "ListItems", e)
            Toast.makeText(this@MainActivity, getString(R.string.toast_wrong_auth), Toast.LENGTH_LONG).show()
        }
    }

    private val activityRequest = ActivityRequest(this)
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var url: String
    }
}
