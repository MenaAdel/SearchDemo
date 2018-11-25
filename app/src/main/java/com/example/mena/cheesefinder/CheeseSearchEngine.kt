package com.example.mena.cheesefinder

class CheeseSearchEngine(private val cheeses: Array<String>) {

  fun search(query: String): List<String> {
    Thread.sleep(2000)
    return cheeses.filter { it.toLowerCase().contains(query.toLowerCase()) }
  }

}