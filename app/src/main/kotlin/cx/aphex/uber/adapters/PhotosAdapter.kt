package cx.aphex.uber.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cx.aphex.uber.R
import cx.aphex.uber.services.FlickrPhoto
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotosAdapter(var photoList: MutableList<FlickrPhoto>,
                    val itemClick: (FlickrPhoto) -> Unit,
                    val searchCallback: (Int) -> Unit) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(v, itemClick)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bindPhoto(photoList[position])

        // if position is the last one, we need to request more
        if (position == photoList.size - 1) {
            // call onSearch
            searchCallback(getCurrentPage() + 1)
        }
    }

    fun getCurrentPage(): Int {
        return Math.ceil(itemCount / 100.0).toInt()
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class ViewHolder(itemView: View, val itemClick: (FlickrPhoto) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindPhoto(photo: FlickrPhoto) {
            with(photo) {
                itemView.photoView
                        .setImageURI(photo.uri)
                itemView.setOnClickListener { itemClick(photo) }
            }
        }
    }

    fun setItems(newPhotos: List<FlickrPhoto>) {
        photoList.clear()
        notifyDataSetChanged()
        addItems(newPhotos)
    }

    fun addItems(newPhotos: List<FlickrPhoto>) {
        newPhotos.forEach {
            photoList.add(it)
            notifyItemInserted(photoList.size - 1) // For one-by-one animations.
        }

    }
}