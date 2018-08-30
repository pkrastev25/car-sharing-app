package com.petar.car.sharing.utils

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.petar.car.sharing.R

object FragmentStackUtil {

    @IdRes
    private const val CONTENT_VIEW_ID = R.id.content_view

    private val fragmentStack = mutableListOf<String>()

    fun navigateToFragmentAndAddToStack(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            fragmentNavId: String
    ) {
        val transaction = fragmentManager.beginTransaction()
        transaction
                .replace(CONTENT_VIEW_ID, fragment, fragmentNavId)
                .addToBackStack(fragmentNavId)
                .commit()

        fragmentStack.add(fragmentNavId)
    }

    fun replaceTopFragmentOnStack(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            fragmentNavId: String
    ) {
        if (fragmentStack.isEmpty()) {
            return
        }

        val topFragment = fragmentManager.findFragmentByTag(fragmentStack.last())
        val transaction = fragmentManager.beginTransaction()
        transaction
                .remove(topFragment)
                .replace(CONTENT_VIEW_ID, fragment, fragmentNavId)
                .addToBackStack(fragmentNavId)
                .commit()

        fragmentStack[fragmentStack.size - 1] = fragmentNavId
    }

    fun showTopFragmentOnStack(
            fragmentManager: FragmentManager
    ) {
        if (fragmentStack.isEmpty()) {
            return
        }

        val topFragment = fragmentManager.findFragmentByTag(fragmentStack.last())
        val transaction = fragmentManager.beginTransaction()
        transaction
                .replace(CONTENT_VIEW_ID, topFragment, fragmentStack.last())
                .commit()
    }

    fun navigateBack(fragmentManager: FragmentManager) {
        if (fragmentStack.isEmpty()) {
            return
        }

        val fragmentToShowTag =
                if (fragmentStack.size > 1) {
                    fragmentStack[fragmentStack.size - 2]
                } else {
                    fragmentStack.last()
                }

        val fragmentToShow = fragmentManager.findFragmentByTag(fragmentToShowTag)
        val transaction = fragmentManager.beginTransaction()
        transaction
                .replace(CONTENT_VIEW_ID, fragmentToShow, fragmentToShowTag)
                .commit()

        fragmentStack.removeAt(fragmentStack.size - 1)
    }

    fun isStackEmpty(): Boolean {
        return fragmentStack.isEmpty()
    }
}