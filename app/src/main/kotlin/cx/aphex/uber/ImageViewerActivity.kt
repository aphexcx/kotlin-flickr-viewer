package cx.aphex.uber

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cx.aphex.uber.services.FlickrPhoto
import kotlinx.android.synthetic.main.activity_image_viewer.*

class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        val photo: FlickrPhoto = intent.getSerializableExtra("photo") as FlickrPhoto

        bigImage.setImageURI(photo.uri)

        imageTitle.text = photo.title
    }
}
