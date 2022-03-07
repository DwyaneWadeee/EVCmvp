package com.evc.evcapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by Android Studio.
 * User: evan
 * Date: 2020/11/17
 * Time: 5:13 PM
 */
class MainFragmentAdapter(fm: FragmentManager,fragments:List<Fragment>,titles:List<String>) : FragmentPagerAdapter(fm) {
    var fm :FragmentManager?=null
    var fragments :List<Fragment>?=null
    var titles :List<String>?=null

    init {
        this.fm = fm
        this.fragments = fragments
        this.titles = titles
    }
    override fun getItem(position: Int): Fragment {
        return fragments?.get(position)!!
    }

    override fun getCount(): Int {
        return fragments?.size!!
    }
}