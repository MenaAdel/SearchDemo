package com.example.mena.cheesefinder.data

import com.example.mena.cheesefinder.EndPoint

class JokesRequest {

    fun fetchJokes(id:String) =
            RetrofitClient.client
                    .create(EndPoint::class.java)
                    .fetchJokes(id)
}