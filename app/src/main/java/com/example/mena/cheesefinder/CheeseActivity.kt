package com.example.mena.cheesefinder

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cheese.*
import java.util.concurrent.TimeUnit

class CheeseActivity : BaseSearchActivity() {

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheese)
    }

    private fun createButtonClickObservable(): Observable<String> {
        // 2
        return Observable.create { emitter ->
            // 3
            searchButton.setOnClickListener {
                // 4
                emitter.onNext(queryEditText.text.toString())
            }

            // 5
            emitter.setCancellable {
                // 6
                searchButton.setOnClickListener(null)
            }
        }
    }

    // 1
    private fun createTextChangeObservable(): Observable<String> {
        // 2
        val textChangeObservable = Observable.create<String> { emitter ->
            // 3
            val textWatcher = object : TextWatcher {

                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                // 4
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }

            }

            // 5
            queryEditText.addTextChangedListener(textWatcher)

            // 6
            emitter.setCancellable {
                queryEditText.removeTextChangedListener(textWatcher)
            }
        }

        // 7
        return textChangeObservable
                .filter { it.length >= 2 }
                .debounce(1000, TimeUnit.MILLISECONDS)
    }


    override fun onStart() {
        super.onStart()
        // 1

        val buttonClickStream = createButtonClickObservable()
        val textChangeStream = createTextChangeObservable()

        val searchTextObservable = Observable.merge<String>(buttonClickStream, textChangeStream)


        disposable = searchTextObservable
                // 1
                .observeOn(AndroidSchedulers.mainThread())
                // 2
                .doOnNext { showProgress() }
                .observeOn(Schedulers.io())
                .map { cheeseSearchEngine.search(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    // 3
                    hideProgress()
                    showResult(it)
                }
    }

    @Override
    override fun onStop() {
        super.onStop()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }


}
