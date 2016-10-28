package cx.aphex.uber.services

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import java.io.Serializable

interface FlickrAPI {
    @GET("services/rest/")
    fun search(@Query("text") text: String, @Query("page") page: Int)
            : Observable<FlickrResponse>
}

// Model:

class FlickrResponse(val photos: FlickrPhotosResponse, val stat: String)

class FlickrPhotosResponse(val page: Int, val pages: Int, val perpage: Int, val total: String, val photo: List<FlickrPhoto>)

class FlickrPhoto(val id: String,
                  val owner: String,
                  val secret: String,
                  val server: String,
                  val farm: Int,
                  val title: String,
                  val ispublic: Int,
                  val isfriend: Int,
                  val isfamily: Int) : Serializable {

    val uri: String
        get() = "http://farm${farm}.static.flickr.com/${server}/${id}_${secret}.jpg"
}
