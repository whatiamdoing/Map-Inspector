package com.mapinspector.di.network

import com.mapinspector.entity.YourPlace
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @PUT("places/{id}/{name}.json")
    fun createPlace(@Path("id") id: String, @Path("name") number: Int, @Body yourPlace: YourPlace): Observable<Response<ResponseBody>>
}