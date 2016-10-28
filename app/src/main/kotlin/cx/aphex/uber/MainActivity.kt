package cx.aphex.uber

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.DimenRes
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cx.aphex.uber.adapters.PhotosAdapter
import cx.aphex.uber.services.APIService
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    private val api: APIService = APIService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPhotoRecyclerView()

        btnSearch.setOnClickListener { onSearch() }
    }

    private val photosAdapter: PhotosAdapter = PhotosAdapter(ArrayList(),
            { photo ->
                // launch imageviewer activity with this photo
                val i = Intent(this, ImageViewerActivity::class.java)
                i.putExtra("photo", photo)
                startActivity(i)
            },
            { page -> onSearch(page) }
    )

    private fun setupPhotoRecyclerView() {
        rvPhotos.setHasFixedSize(true)
        rvPhotos.layoutManager = GridLayoutManager(this, 3)
        rvPhotos.addItemDecoration(ItemOffsetDecoration(rvPhotos.context, R.dimen.item_offset))
        rvPhotos.itemAnimator = ScaleInAnimator()
        rvPhotos.adapter = photosAdapter
    }

    fun onSearch(page: Int = 1) {
        api.searchFlickr(etSearch.text.toString(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.photos.photo }
                .subscribe {
                    if (page == 1)
                        photosAdapter.setItems(it)
                    else
                        photosAdapter.addItems(it)
                }
    }

}

class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(@NonNull context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId)) {
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}



