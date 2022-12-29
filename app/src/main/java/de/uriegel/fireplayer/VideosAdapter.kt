//package de.uriegel.fireplayer
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.recyclerview.widget.RecyclerView
//import de.uriegel.fireplayer.Extensions.isFilm
//
//class VideosAdapter(private val films: Array<String>, private val clickListener: ((track: String)->Unit)) : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
//
//    override fun getItemCount(): Int {
//        return films.count()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.video, parent, false)
//        return ViewHolder(v, clickListener)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.film = films[position]
//        holder.videoNameView.text =
//            if (holder.film.isFilm())
//                holder.film.substring(0, holder.film.length - 4)
//            else
//                holder.film
//        holder.videoFolderView.text = holder.film
//        holder.videoNameView.visibility = if (holder.film.isFilm()) View.VISIBLE else View.GONE
//        holder.videoFolderView.visibility = if (holder.film.isFilm()) View.GONE else View.VISIBLE
//    }
//
//    fun containsEqualFilms(filmListToCompare: Array<String>): Boolean {
//        return films.contentEquals(filmListToCompare)
//    }
//
//    class ViewHolder(view: View, val clickListener: ((film: String)->Unit)) : RecyclerView.ViewHolder(view) {
//        val videoNameView: Button = view.findViewById(R.id.videoNameView)
//        val videoFolderView: Button = view.findViewById(R.id.videoFolderView)
//        var film = ""
//        init {
//            videoNameView.setOnClickListener {clickListener(film) }
//            videoFolderView.setOnClickListener {clickListener(film) }
//            videoFolderView.visibility = View.GONE
//            videoNameView.visibility = View.GONE
//        }
//    }
//}