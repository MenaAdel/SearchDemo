package com.example.mena.cheesefinder

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.mena.cheesefinder.data.JokesRequest
import com.example.mena.cheesefinder.data.JokesResponse
import com.example.mena.cheesefinder.data.RetrofitClient
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response

class JokesViewModel : ViewModel() {

    private val retrofitClient: RetrofitClient
    private var jokesRequest : JokesRequest
    private var disposables: CompositeDisposable
    private var data:MutableLiveData<JokesResponse>
    private val isLoading: MutableLiveData<Boolean>
    private val textChangeSubject = BehaviorSubject.create<String>()
    private var errorMessage : MutableLiveData<String>

    init {
        this.retrofitClient = RetrofitClient
        this.disposables = CompositeDisposable()
        this.jokesRequest = JokesRequest()
        this.isLoading = MutableLiveData()
        this.data = MutableLiveData()
        this.errorMessage = MutableLiveData()
    }

    fun getData(): LiveData<JokesResponse> {
        if (data.value == null) {
            subscribeToTextChanges()
        }
        return data
    }

    private fun subscribeToTextChanges() {
        val disposable = textChangeSubject.observeOn(AndroidSchedulers.mainThread())
                .doOnNext { isLoading.value = true }
                .filter { it.length >= 2 }
                .observeOn(Schedulers.io())
                .switchMapSingle { fetchJokesData(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable? ->
                    errorMessage.value = t.toString()
                }
                .subscribe {
                    isLoading.value = false
                    data.value = it.body()!!
                }
        disposables += disposable
    }

    fun onSearchTextChanges(text: String) {
        textChangeSubject.onNext(text)
    }

    private fun fetchJokesData(id:String): Single<Response<JokesResponse>> {
        return jokesRequest.fetchJokes(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun viewErrorState() : LiveData<String>{
        return errorMessage
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}