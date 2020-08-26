package com.kurmakaeva.anastasia.lockquote.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kurmakaeva.anastasia.lockquote.R
import com.kurmakaeva.anastasia.lockquote.ui.SearchActivity
import com.kurmakaeva.anastasia.lockquote.viewmodel.SearchViewModel.*

class GeniusSongSearchAdapter(
    private var songSummaryViewList: List<SongSummaryViewData>?,
    private val songListAdapterListener: SongSearchAdapterListener,
    private val parentActivity: SearchActivity
): RecyclerView.Adapter<GeniusSongSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.song_search_item, parent, false), songListAdapterListener)
    }

    override fun getItemCount(): Int {
        return songSummaryViewList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchViewList = songSummaryViewList ?: return
        val searchView = searchViewList[position]
        holder.songSummaryViewData = searchView
        holder.songNameTextView.text = searchView.title
        holder.artistNameTextView.text = searchView.name

        Glide.with(parentActivity)
            .load(searchView.header_image_thumbnail_url)
            .into(holder.songThumbnail)
    }

    interface SongSearchAdapterListener {
        fun onShowDetails(songSummaryViewData: SongSummaryViewData) {

        }
    }

        inner class ViewHolder(v: View, private val songSearchAdapterListener: SongSearchAdapterListener) : RecyclerView.ViewHolder(v) {
            var songSummaryViewData: SongSummaryViewData? = null
            val songNameTextView: TextView = v.findViewById(R.id.song_name)
            val artistNameTextView: TextView = v.findViewById(R.id.artist_name)
            val songThumbnail: ImageView = v.findViewById(R.id.songThumbnail)

            init {
                v.setOnClickListener {
                    songSummaryViewData?.let {
                        songSearchAdapterListener.onShowDetails(it)
                    }
                }
            }
        }

    fun setSearchData(songSummaryViewData: List<SongSummaryViewData>) {
        songSummaryViewList = songSummaryViewData
        this.notifyDataSetChanged()
    }
}
