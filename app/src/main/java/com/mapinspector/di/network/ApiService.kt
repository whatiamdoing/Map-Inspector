package com.mapinspector.di.network

import com.mapinspector.enity.Place
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @PUT("users/{id}/{name}.json")
    fun createPlace(@Path("id") id: String, @Path("name") placeId: String, @Body yourPlace: Place): Observable<Response<ResponseBody>>

    @GET("users/{id}.json")
    fun getPlaces(@Path("id") id: String): Observable<Map<String, Place>>
}