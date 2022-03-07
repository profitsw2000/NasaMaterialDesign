package ru.profitsw2000.nasamaterialdesign.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.profitsw2000.nasamaterialdesign.representation.PODServerResponseData
import ru.profitsw2000.nasamaterialdesign.representation.earth.EarthServerResponseData

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDayForDate(@Query("api_key") apiKey: String,@Query("date")  date:String): Call<PODServerResponseData>

    //
    @GET("planetary/earth/imagery")
    fun getEarthImagery(@Query("lat") latitude: String,
                        @Query("lon") longitude: String,
                        @Query("date") date: String,
                        @Query("dim") dimension: Float,
                        @Query("api_key") apiKey: String): Call<EarthServerResponseData>

}
