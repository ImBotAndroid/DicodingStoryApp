package com.example.submissiondicodingintermediate_1.ui.maps

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submissiondicodingintermediate_1.R
import com.example.submissiondicodingintermediate_1.data.local.DetailStory
import com.example.submissiondicodingintermediate_1.data.response.ListStoryItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.submissiondicodingintermediate_1.databinding.ActivityMapsBinding
import com.example.submissiondicodingintermediate_1.ui.ViewModelFactory
import com.example.submissiondicodingintermediate_1.ui.detail.DetailStoryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setUpView()
        setUpViewModel()
        setUpStoriesLocation()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
    private fun setUpStoriesLocation() {
        val preference = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val bearer = "Bearer " + preference.getString("token", "").toString()

        mapsViewModel.storyLocation(bearer).observe(this){
            showStoryLocation(it)
        }
    }

    private fun showStoryLocation(location: List<ListStoryItem>) {

        val boundsBuilder = LatLngBounds.Builder()
        location.forEach { storyLocation ->
            val latLng = LatLng(storyLocation.lat, storyLocation.lon)
            val addressLocation = getAddressLocation(storyLocation.lat, storyLocation.lon)

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(storyLocation.name)
                    .snippet(addressLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

        mMap.setOnMarkerClickListener { marker ->
            val markerPosition = marker.position

            for (i in location.indices) {
                if (markerPosition.latitude == location[i].lat && markerPosition.longitude == location[i].lon) {
                    setUpCardView(location, i)
                }
            }
            val cameraPosition = CameraPosition.Builder().target(markerPosition).zoom(8.5f).build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            marker.showInfoWindow()

            true
        }
    }

    private fun getAddressLocation(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].subLocality + ", " + list[0].locality + ", " + list[0].subAdminArea
                Log.d(TAG, "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun setUpCardView(location: List<ListStoryItem>, i: Int) {
        binding.cvDetailInfo.visibility = View.VISIBLE

        val username = location[i].name
        val description = location[i].description
        val photo = location[i].photoUrl

        binding.tvNameInfo.text = username
        binding.tvDescriptionInfo.text = description

        Glide.with(this)
            .load(photo)
            .into(binding.ivImageInfo)

        binding.cvDetailInfo.setOnClickListener {
            val data = DetailStory(username, description, photo)
            val intent = Intent(this@MapsActivity, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.EXTRA_DATA, data)
            startActivity(intent)
        }
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun setUpViewModel() {
        mapsViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[MapsViewModel::class.java]
    }

    companion object {
        const val TAG = "Maps Activity"
    }
}