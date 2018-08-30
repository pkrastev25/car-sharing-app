package com.petar.car.sharing.interfaces

import android.support.v4.app.Fragment

interface INavigationEvents {

    fun replaceTopFragmentOnStack(
            fragment: Fragment,
            fragmentNavId: String
    )
}