package ru.profitsw2000.nasamaterialdesign.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.profitsw2000.nasamaterialdesign.representation.PODServerResponseData
import ru.profitsw2000.nasamaterialdesign.representation.earth.EarthServerResponseData
import ru.profitsw2000.nasamaterialdesign.representation.epic.EPICServerResponseData
import ru.profitsw2000.nasamaterialdesign.representation.mars.MRFServerResponseData
import ru.profitsw2000.nasamaterialdesign.ui.fragments.navigation.EPICFragment

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDayForDate(@Query("api_key") apiKey: String,@Query("date")  date:String): Call<PODServerResponseData>

    //Снимки земли
    @GET("planetary/earth/assets")
    fun getEarthImagery( @Query("lon") longitude: String,
                         @Query("lat") latitude: String,
                         @Query("date") date: String,
                         @Query("dim") dimension: String,
                         @Query("api_key") apiKey: String): Call<EarthServerResponseData>

    //Снимки с марсохода Curiocity
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsRoverPhoto( @Query("earth_date") earth_date: String,
                         @Query("api_key") apiKey: String): Call<MRFServerResponseData>

    // Earth Polychromatic Imaging Camera (EPIC)
    @GET("EPIC/api/natural/date/{date}")
    fun getEPICPhoto(@Path("date") date: String,
                     @Query("api_key") apiKey: String): Call<EPICServerResponseData>

}
