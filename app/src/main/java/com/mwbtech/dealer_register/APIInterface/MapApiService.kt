package com.mwbtech.dealer_register.APIInterface

import com.mwbtech.dealer_register.map.PlacePredictions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MapApiService {
    @GET("maps/api/place/autocomplete/json")
    fun getSearchPlaces(@QueryMap path:HashMap<String,String>): Call<PlacePredictions>
}