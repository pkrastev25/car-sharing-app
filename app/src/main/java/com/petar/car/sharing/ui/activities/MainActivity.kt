package com.petar.car.sharing.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.petar.car.sharing.R
import com.petar.car.sharing.interfaces.INavigationEvents
import com.petar.car.sharing.ui.fragments.PlaceMarkListFragment
import com.petar.car.sharing.utils.FragmentStackUtil
import com.petar.car.sharing.utils.PermissionsUtil

class MainActivity : AppCompatActivity(), INavigationEvents {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (PermissionsUtil.areAllPermissionsGranted(this)) {
            showStartFragment()
        } else {
            requestPermissions()
        }
    }

    override fun onBackPressed() {
        FragmentStackUtil.navigateBack(supportFragmentManager)

        if (FragmentStackUtil.isStackEmpty()) {
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                val areAllPermissionsGranted = grantResults.all {
                    it == PackageManager.PERMISSION_GRANTED
                }

                if (areAllPermissionsGranted) {
                    showStartFragment()
                } else {
                    Toast.makeText(this, R.string.hint_permissions, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun replaceTopFragmentOnStack(fragment: Fragment, fragmentNavId: String) {
        FragmentStackUtil.replaceTopFragmentOnStack(
                supportFragmentManager,
                fragment,
                fragmentNavId
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_CODE
        )
    }

    private fun showStartFragment() {
        if (FragmentStackUtil.isStackEmpty()) {
            FragmentStackUtil.navigateToFragmentAndAddToStack(
                    supportFragmentManager,
                    PlaceMarkListFragment.newInstance(),
                    PlaceMarkListFragment.NAV_ID
            )
        } else {
            FragmentStackUtil.showTopFragmentOnStack(supportFragmentManager)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 0
    }
}
