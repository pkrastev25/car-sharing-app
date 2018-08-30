package com.petar.car.sharing.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petar.car.sharing.R
import com.petar.car.sharing.interfaces.INavigationEvents
import com.petar.car.sharing.models.AppStateModel
import com.petar.car.sharing.models.PlaceMarkModel
import com.petar.car.sharing.ui.adapters.PlaceMarkAdapter
import com.petar.car.sharing.viewmodels.PlaceMarksViewModel
import kotlinx.android.synthetic.main.fragment_place_mark_list.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_loading.*

class PlaceMarkListFragment : Fragment() {

    private val placeMarkAdapter = PlaceMarkAdapter()
    private lateinit var placeMarksViewModel: PlaceMarksViewModel
    private var navigationEvents: INavigationEvents? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        navigationEvents = context as INavigationEvents
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_mark_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recycler.layoutManager = LinearLayoutManager(context)
        view_recycler.adapter = placeMarkAdapter

        show_map_button.setOnClickListener {
            navigationEvents?.replaceTopFragmentOnStack(
                    PlaceMarkMapFragment.newInstance(),
                    PlaceMarkMapFragment.NAV_ID
            )
        }

        retry_button.setOnClickListener {
            placeMarksViewModel.loadPlaceMarks()
        }

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

    override fun onDestroy() {
        super.onDestroy()

        navigationEvents = null
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

    companion object {
        const val NAV_ID = "PLACE_MARK_LIST_FRAGMENT_NAV_ID"

        fun newInstance(): Fragment {
            return PlaceMarkListFragment()
        }
    }
}
