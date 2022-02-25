package ru.profitsw2000.nasamaterialdesign.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.profitsw2000.nasamaterialdesign.representation.PODServerResponseData

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDayForDate(@Query("api_key") apiKey: String,@Query("date")  date:String): Call<PODServerResponseData>
}
