package com.mapinspector.di.network

import com.mapinspector.db.enity.Place
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @PUT("users/{id}/{name}.json")
    fun createPlace(@Path("id") id: String, @Path("name") placeId: String, @Body yourPlace: Place): Observable<Response<ResponseBody>>

    @GET("users/{id}.json")
    fun getPlaces(@Path("id") id: String): Observable<Map<String, Place>>

    @DELETE("users/{id}/{placeId}.json")
    fun deletePlace(@Path("id") id: String, @Path("placeId") placeId: String): Observable<Response<ResponseBody>>
}