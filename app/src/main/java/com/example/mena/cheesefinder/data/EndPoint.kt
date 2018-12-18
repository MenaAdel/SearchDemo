package com.example.mena.cheesefinder

import com.example.mena.cheesefinder.data.JokesResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface EndPoint {
    @GET("jokes/{id}")
    fun fetchJokes(@Path("id") id:String): Single<Response<JokesResponse>>
}