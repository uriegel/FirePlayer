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
import de.uriegel.fireplayer.Extensions.isFilm
import de.uriegel.fireplayer.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.net.URLEncoder

class MainActivityTest : AppCompatActivity() {
// TODO Update gradle for Compose
// TODO Create MainActivity ans MainScreen
// TODO Adapt FirePlayer dark theme

//    override val coroutineContext = Dispatchers.Main
//
//    @Serializable
//    data class Files(val files: List<String>)

    override fun onCreate(savedInstanceState: Bundle?) {
//        fun isTV(): Boolean { return android.os.Build.MODEL.contains("AFT") }
//        if (isTV())
//            setTheme(R.style.AppTheme_FullScreen)

        super.onCreate(savedInstanceState)

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.videos.layoutManager = GridLayoutManager(this, 6)
//        binding.videos.adapter = VideosAdapter(emptyArray(), ::onItemClick)
//        binding.videos.setHasFixedSize(true)
    }

//    override fun onResume() {
//        super.onResume()
//
//        launch {
//            initialize()
//            accessDisk()
//            listItems()
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putStringArray(STATE_URL_PARTS, urlParts)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        savedInstanceState.getStringArray(STATE_URL_PARTS)?.let {
//            urlParts = it.toList().toTypedArray()
//        }
//    }
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_MENU)
//            showSettings()
//        return super.onKeyDown(keyCode, event)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id: Int = item.itemId
//        if (id == R.id.menu_settings) {
//            showSettings()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onBackPressed() {
//        if (urlParts.size > 1) {
//            urlParts = urlParts.toList().dropLast(1).toTypedArray()
//            listItems()
//        }
//        else
//            onBackPressedDispatcher.onBackPressed()
//    }
//
//    private fun showSettings() {
//        launch {
//            activityRequest.launch(Intent(this@MainActivity, SettingsActivity::class.java))
//            initialize()
//            accessDisk()
//            listItems()
//        }
//    }
//
//    private suspend fun initialize() {
//        try {
//            if (urlParts.isEmpty()) {
//                val preferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
//                var url = preferences.getString("url", "")
//                if (url!!.length < 6) {
//                    activityRequest.launch(Intent(this@MainActivity, SettingsActivity::class.java))
//                    url = preferences.getString("url", "")
//                }
//                urlParts = arrayOf("${url}/video")
//
//                basicAuthentication(preferences.getString("name", "")!!, preferences.getString("auth_pw", "")!!)
//                MainActivity.url = url!!
//            }
//        } catch (e: Exception) {
//            Log.w("FP", "Initialize", e)
//            Toast.makeText(this@MainActivity, getString(R.string.toast_wrong_auth), Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun onItemClick(path: String) {
//        if (path.endsWith(".mp4", true) or path.endsWith(".mkv", true)) {
//            val film = (urlParts + URLEncoder.encode(path, "utf-8"))
//                .joinToString(separator = "/")
//                .replace("+", "%20")
//            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
//            intent.putExtra("film", film)
//            startActivity(intent)
//        } else {
//            urlParts += URLEncoder.encode(path, "utf-8")
//            listItems()
//        }
//    }
//
//    private fun listItems() {
//        launch {
//            try {
//                val folderComparator = compareBy<String>{ it.isFilm() }
//                val fileTypeThenStringComparator = folderComparator.thenBy { it }
//
//                val result = getString(urlParts.joinToString(separator = "/").replace("+", "%20"))
//                val files = Json
//                    .decodeFromString<Files>(result)
//                    .files
//                    .sortedWith(fileTypeThenStringComparator)
//                    .toTypedArray()
//                if (!(binding.videos.adapter as VideosAdapter).containsEqualFilms(files))
//                    binding.videos.adapter = VideosAdapter(files, ::onItemClick)
//            } catch (e: Exception) {
//                Log.w("FP", "ListItems", e)
//                Toast.makeText(this@MainActivity, getString(R.string.toast_wrong_auth), Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    private val activityRequest = ActivityRequest(this)
//    private var urlParts = arrayOf<String>()
//    private lateinit var binding: ActivityMainBinding
//
//    companion object {
//        lateinit var url: String
//        const val STATE_URL_PARTS = "STATE_URL_PARTS"
//    }
}
