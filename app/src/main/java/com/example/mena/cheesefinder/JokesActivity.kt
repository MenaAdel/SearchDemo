package com.example.mena.cheesefinder

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.mena.cheesefinder.data.JokesResponse
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.textChangeEvents
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_cheese.*

class JokesActivity : AppCompatActivity() {

    private lateinit var disposable: Disposable
    private lateinit var viewModel : JokesViewModel
    private var jokesData: JokesResponse = JokesResponse()
    private val cheeseAdapter = JokesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheese)

        initViewModel()

        initRecyclerView()

        queryEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onSearchTextChanges(s.toString())            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                showProgress()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //viewModel.onSearchTextChanges(s.toString())
                hideProgress()
            }
        })
        observeJokesData()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this)
                .get(JokesViewModel::class.java)
    }

    private fun observeJokesData(){
        viewModel.getData().observe(this , Observer<JokesResponse> {
            bindJokesData(it)
        })

        viewModel.viewErrorState().observe(this , Observer<String> { showError(it!!) })
    }

    private fun bindJokesData(jokesData: JokesResponse?){
        this.jokesData = jokesData!!
        showResult(jokesData.value.joke)
    }

    private fun initRecyclerView(){
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = cheeseAdapter
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showError(errorMessage : String){
        Toast.makeText(this ,errorMessage ,Toast.LENGTH_LONG).show()
    }

    private fun showResult(result: String) {
        if (result.isEmpty()) {
            Toast.makeText(this, R.string.nothing_found, Toast.LENGTH_SHORT).show()
        }
        //cheeseAdapter.cheeses = result
        txtJoke.text = result
    }

    @Override
    override fun onStop() {
        super.onStop()
    }


}
