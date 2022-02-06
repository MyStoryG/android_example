package com.example.retrofit2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.example.retrofit2.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("my:imgRes")
fun setImageViewResource(imageView: ImageView, resId: Bitmap) {
    imageView.setImageBitmap(resId)
}

class MainActivity : AppCompatActivity() {
    var mDataHandler: DataHandler? = null
    private lateinit var retrofit: Retrofit
    private lateinit var youtubeService: RetrofitYouTubeService
    lateinit var bmp: Bitmap
    var videoTitle: String = ""
    var uploadDate: String = ""
    lateinit var previewImageView: ImageView
    lateinit var titleTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bmp = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_launcher)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.test = this
        mDataHandler = DataHandler()
        retrofit = RetrofitYouTubeData.getInstance()
        youtubeService = retrofit.create(RetrofitYouTubeService::class.java)
        previewImageView = findViewById<ImageView>(R.id.previewIv)
        titleTextView = findViewById<TextView>(R.id.titleTv)
        dateTextView = findViewById<TextView>(R.id.dateTv)
        loadData()
    }


    fun loadData() {
        //https://www.youtube.com/watch?v=dyRsYk0LyA8
        youtubeService.requestVideoInformation(
                "dyRsYk0LyA8"
        ).enqueue(object : Callback<Video> {
            override fun onFailure(call: Call<Video>, t: Throwable) {
            }

            override fun onResponse(call: Call<Video>, response: Response<Video>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val localDate: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    val date: Date = localDate.parse(result?.items?.get(0)?.snippet?.publishedAt)
                    val uploadDateFormat = SimpleDateFormat("yyyy-MM-dd").format(date)
                    videoTitle = result?.items?.get(0)?.snippet?.title!!
                    uploadDate = uploadDateFormat.toString()

                    Thread(Runnable {
                        while (true) {
                            if (result?.items?.get(0)?.snippet?.thumbnails?.high?.url != null) {
                                bmp = BitmapFactory.decodeStream(
                                        URL(result?.items?.get(0)?.snippet?.thumbnails?.high?.url).openConnection()
                                                .getInputStream()
                                )
                            }

                            if (bmp != null) {
                                break
                            }
                        }
                        binding.invalidateAll()
//                        mDataHandler?.sendEmptyMessage(1)
                    }).start()
                }
            }
        })
    }

    inner class DataHandler : Handler() {
        override fun handleMessage(msg: Message) {
            previewImageView.setImageBitmap(bmp)
            titleTextView.text = videoTitle
            dateTextView.text = uploadDate
        }
    }
}