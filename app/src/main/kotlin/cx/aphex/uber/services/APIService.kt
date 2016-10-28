package cx.aphex.uber.services

import cx.aphex.uber.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import rx.Observable

class APIService {
    private val flickrApi: FlickrAPI

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getHttpClient())
                .build()

        flickrApi = retrofit.create(FlickrAPI::class.java)
    }

    fun searchFlickr(text: String, page: Int): Observable<FlickrResponse> {
        return flickrApi.search(text, page)
    }

    //https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&text=kittens

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newUrl = chain.request().url().newBuilder()
                    .addQueryParameter("method", "flickr.photos.search")
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .addQueryParameter("format", "json")
                    .addQueryParameter("nojsoncallback", "1")
                    .build()
            val newRequest = chain.request().newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }.build()
    }

}