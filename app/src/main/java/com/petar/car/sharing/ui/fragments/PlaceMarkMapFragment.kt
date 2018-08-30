package com.petar.car.sharing.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.petar.car.sharing.R
import com.petar.car.sharing.constants.Constants
import com.petar.car.sharing.interfaces.INavigationEvents
import com.petar.car.sharing.models.AppStateModel
import com.petar.car.sharing.models.PlaceMarkModel
import com.petar.car.sharing.utils.ServicesUtil
import com.petar.car.sharing.viewmodels.MapViewModel
import com.petar.car.sharing.viewmodels.PlaceMarksViewModel
import kotlinx.android.synthetic.main.fragment_place_mark_map.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_loading.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class PlaceMarkMapFragment : Fragment() {

    private val mapOverlayList = arrayListOf<Overlay>()
    private lateinit var placeMarksViewModel: PlaceMarksViewModel
    private lateinit var mapViewModel: MapViewModel
    private lateinit var userLocationOverlay: MyLocationNewOverlay
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
        return inflater.inflate(R.layout.fragment_place_mark_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        show_list_button.setOnClickListener {
            navigationEvents?.replaceTopFragmentOnStack(
                    PlaceMarkListFragment.newInstance(),
                    PlaceMarkListFragment.NAV_ID
            )
        }

        retry_button.setOnClickListener {
            placeMarksViewModel.loadPlaceMarks()
        }

        configureMap()
        configureUserLocationMarker()

        placeMarksViewModel = ViewModelProviders.of(this).get(PlaceMarksViewModel::class.java)
        placeMarksViewModel.getPlaceMarksLiveData().observe(this, Observer<AppStateModel> {
            when (it) {
                is AppStateModel.LoadingState -> showLoadingView()
                is AppStateModel.DataState<*> -> {
                    renderMarkers(it.data as List<PlaceMarkModel>)
                    it.data.firstOrNull {
                        it.coordinates.size > 2
                    }?.apply {
                        adjustUserViewToPosition(this.coordinates[1], this.coordinates[0])
                    }
                    showDataView()
                }
                is AppStateModel.ErrorState -> showErrorView()
            }
        })
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        mapViewModel.getLastMapPositionLiveData().observe(this, Observer<List<Double>> {
            if (it?.isNotEmpty() == true) {
                adjustUserViewToPosition(
                        it[0],
                        it[1],
                        it[2]
                )
            }
        })
    }

    override fun onStart() {
        super.onStart()

        placeMarksViewModel.getCachedPlaceMarks()
    }

    override fun onResume() {
        super.onResume()

        view_map.onResume()

        context?.let {
            if (!ServicesUtil.isGpsProviderEnabled(it)) {
                Toast.makeText(it, R.string.hint_user_location, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        view_map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mapViewModel.saveMapPosition(
                view_map.mapCenter.latitude,
                view_map.mapCenter.longitude,
                view_map.zoomLevelDouble
        )
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

    private fun configureMap() {
        view_map.setBuiltInZoomControls(false)
        view_map.setMultiTouchControls(true)
        view_map.maxZoomLevel = Constants.MAP_MAX_ZOOM
        view_map.minZoomLevel = Constants.MAP_MIN_ZOOM
        view_map.controller.setZoom(Constants.MAP_DEFAULT_ZOOM)
    }

    private fun configureUserLocationMarker() {
        userLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), view_map)
        userLocationOverlay.enableMyLocation()
        userLocationOverlay.runOnFirstFix {
            adjustUserViewToPosition(
                    userLocationOverlay.lastFix.latitude,
                    userLocationOverlay.lastFix.longitude
            )
        }
        mapOverlayList.add(userLocationOverlay)
    }

    private fun renderMarkers(list: List<PlaceMarkModel>) {
        list.filter {
            it.coordinates.size > 2
        }.forEach {
            mapOverlayList.add(createMarker(it))
        }

        view_map.overlays.addAll(mapOverlayList)
    }

    private fun adjustUserViewToPosition(
            latitude: Double,
            longitude: Double,
            zoomLevel: Double = view_map.zoomLevelDouble
    ) {
        activity?.runOnUiThread {
            view_map.controller.apply {
                animateTo(GeoPoint(latitude, longitude))
                setZoom(zoomLevel)
            }
        }
    }

    private fun createMarker(placeMarkModel: PlaceMarkModel): Marker {
        return Marker(view_map).apply {
            position = GeoPoint(placeMarkModel.coordinates[1], placeMarkModel.coordinates[0])
            title = placeMarkModel.name
            setOnMarkerClickListener(createOnMarkerClickListener())
        }
    }

    private fun createOnMarkerClickListener(): Marker.OnMarkerClickListener {
        return Marker.OnMarkerClickListener { marker, mapView ->
            val overlaysDisplayedOnMapCount = mapView.overlays.count()
            mapView.overlays.clear()

            if (overlaysDisplayedOnMapCount == 2) {
                InfoWindow.closeAllInfoWindowsOn(mapView)
                mapView.overlays.addAll(mapOverlayList)
            } else {
                mapView.overlays.add(marker)
                mapView.overlays.add(userLocationOverlay)
                marker.showInfoWindow()
            }

            mapView.controller.animateTo(marker.position)

            true
        }
    }

    companion object {
        const val NAV_ID = "PLACE_MARK_MAP_FRAGMENT_NAV_ID"

        fun newInstance(): Fragment {
            return PlaceMarkMapFragment()
        }
    }
}
