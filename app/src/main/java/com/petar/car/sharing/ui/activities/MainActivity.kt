package com.petar.car.sharing.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.petar.car.sharing.R
import com.petar.car.sharing.models.AppStateModel
import com.petar.car.sharing.models.PlaceMarkModel
import com.petar.car.sharing.ui.adapters.PlaceMarkAdapter
import com.petar.car.sharing.viewmodels.PlaceMarksViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_loading.*

class MainActivity : AppCompatActivity() {

    private lateinit var placeMarksViewModel: PlaceMarksViewModel
    private val placeMarkAdapter = PlaceMarkAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_data.layoutManager = LinearLayoutManager(this)
        view_data.adapter = placeMarkAdapter

        placeMarksViewModel = ViewModelProviders.of(this).get(PlaceMarksViewModel::class.java)
        placeMarksViewModel.getPlaceMarksLiveData().observe(this, Observer<AppStateModel> {
            when (it) {
                is AppStateModel.LoadingState -> showLoadingView()
                is AppStateModel.DataState<*> -> {
                    placeMarkAdapter.setItems(it.data as List<PlaceMarkModel>)
                    showDataView()
                }
                is AppStateModel.ErrorState -> showErrorView()
            }
        })
    }

    override fun onStart() {
        super.onStart()

        placeMarksViewModel.getCachedPlaceMarks()
    }

    private fun showLoadingView() {
        view_loading.visibility = View.VISIBLE
        view_data.visibility = View.INVISIBLE
        view_error.visibility = View.INVISIBLE
    }

    private fun showDataView() {
        view_loading.visibility = View.INVISIBLE
        view_data.visibility = View.VISIBLE
        view_error.visibility = View.INVISIBLE
    }

    private fun showErrorView() {
        view_loading.visibility = View.INVISIBLE
        view_data.visibility = View.INVISIBLE
        view_error.visibility = View.VISIBLE
    }
}
