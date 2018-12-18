package com.example.mena.cheesefinder.data

class JokesResponse {
    lateinit var type : String
    lateinit var value : Value

    class Value{
        var id : Int = 0
        lateinit var joke : String
        lateinit var categories : List<String>
    }
}