package ru.profitsw2000.nasamaterialdesign.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.profitsw2000.nasamaterialdesign.representation.earth.EarthServerResponseData
import ru.profitsw2000.nasamaterialdesign.retrofit.PODRetrofitImpl
import ru.profitsw2000.nasamaterialdesign.utils.convertESRDToPODSRD

class MEEViewModel (
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getEarthImageryData(latitude: String,
                            longitude: String,
                            date: String,
                            dim: Float): LiveData<PictureOfTheDayData> {
        sendServerRequestForEarthImagery(latitude, longitude, date, dim)
        return liveDataForViewToObserve
    }

    private fun sendServerRequestForEarthImagery(latitude: String, longitude: String, date: String, dim: Float) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey = "Mg2woVFc6hKt5xjKlvUKn98AGStgNooNb2AGolcN"
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getEarthImagery(latitude, longitude, date, dim, apiKey).enqueue(object :
                Callback<EarthServerResponseData> {
                override fun onResponse(
                    call: Call<EarthServerResponseData>,
                    response: Response<EarthServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            PictureOfTheDayData.Success(convertESRDToPODSRD(response.body()!!))
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<EarthServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }
            })
        }
    }
}