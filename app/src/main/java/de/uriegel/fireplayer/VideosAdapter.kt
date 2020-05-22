package de.uriegel.fireplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VideosAdapter(private val films: Array<String>, private val clickListener: ((track: String)->Unit)) : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return films.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.video, parent, false)
        return ViewHolder(v, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.film = films[position]
        holder.videoNameView.text = holder.film
        holder.videoNameView.setOnClickListener { holder.clickListener(holder.film) }
    }

    class ViewHolder(view: View, val clickListener: ((film: String)->Unit)) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {clickListener(film) }
        }
        var film = ""
        val videoNameView: TextView = view.findViewById(R.id.videoNameView)
    }
}