package com.evc.evcapp.activity

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.evc.commonui.DragFloatActionButton
import com.evc.evcapp.BuildConfig
import com.evc.evcapp.R
import com.evc.evcapp.adapter.MainFragmentAdapter
import com.evc.evcapp.bean.TokenBean
import com.evc.evcapp.fragment.TestFragment
import com.evc.evcapp.presenter.MainPresenter
import com.evc.evcapp.view.MainView
import com.evc.foundation.kt.BaseKtActivity
import com.evc.toolkit.util.EventConstant
import com.google.android.material.tabs.TabLayout
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_main_tab_layout.view.*

class MainActivity : BaseKtActivity<MainPresenter>(),
    MainView {
    val titles = ArrayList<String>()
    val fragments = ArrayList<Fragment>()
    private var mPressedTime: Long = 0

    override fun setContentViewToRoot(): View {
        return layoutInflater.inflate(R.layout.activity_main,null)
    }

    override fun initView() {
        super.initView()
    }

    override fun initData() {
        super.initData()

        initTestBtn()
        initPermissions()
        initTab()

        // TODO: 检查更新
        // TODO: 登录状态处理

        presenter.toObservable(String::class.java,
            Consumer {
                if (it.equals(EventConstant.TEST_WORK)){
                    showToast("test")
                }
            })
    }

    private fun initFragments() {
        val signMainFragment = TestFragment()
        val alertMainFragment = TestFragment()
        val tableFragment = TestFragment()
        val mineFragment = TestFragment()

        var signFragmentTitle = "Tab1"
        var alertFragmentTitle = "Tab2"
        var tableFragmentTitle = "Tab3"
        var mineFragmentTitle = "Tab4"

        fragments.clear()
        fragments.add(signMainFragment)
        fragments.add(alertMainFragment)
        fragments.add(tableFragment)
        fragments.add(mineFragment)

        titles.clear()
        titles.add(signFragmentTitle)
        titles.add(alertFragmentTitle)
        titles.add(tableFragmentTitle)
        titles.add(mineFragmentTitle)
    }

    private fun initTab() {
        initFragments()

        val fargmentAdapter = MainFragmentAdapter(supportFragmentManager, fragments, titles)
        viewpager.adapter = fargmentAdapter
        viewpager.offscreenPageLimit=4
        viewpager.setNoScroll(false)
        tabs.setupWithViewPager(viewpager,true)

        val v0 = LayoutInflater.from(applicationContext).inflate(R.layout.item_main_tab_layout, null)
        v0.imageview.setImageResource(R.drawable.ic_main_tab_sign)
        v0.tab_tv.setText(titles[0])
        tabs.getTabAt(0)?.setCustomView(v0)

        val v1 = LayoutInflater.from(applicationContext).inflate(R.layout.item_main_tab_layout, null)
        v1.imageview.setImageResource(R.drawable.ic_main_tab_alert)
        v1.tab_tv.setText(titles[1])
        tabs.getTabAt(1)?.setCustomView(v1)

        val v2 = LayoutInflater.from(applicationContext).inflate(R.layout.item_main_tab_layout, null)
        v2.imageview.setImageResource(R.drawable.ic_main_tab_table)
        v2.tab_tv.setText(titles[2])
        tabs.getTabAt(2)?.setCustomView(v2)

        val v3 = LayoutInflater.from(applicationContext).inflate(R.layout.item_main_tab_layout, null)
        v3.imageview.setImageResource(R.drawable.ic_main_tab_mine)
        v3.tab_tv.setText(titles[3])
        tabs.getTabAt(3)?.setCustomView(v3)

        tabs.setSelectedTabIndicatorHeight(0)
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tabs.selectedTabPosition == 0) {
                    presenter.post(EventConstant.TEST_WORK)
                }
            }
        })

    }

    private fun initTestBtn() {
        val imageButton: DragFloatActionButton = findViewById(R.id.fb) as DragFloatActionButton

        if (BuildConfig.DEBUG) {
            imageButton.show()
            imageButton.setOnClickListener(View.OnClickListener {
                //单击
            })
            imageButton.setOnLongClickListener(View.OnLongClickListener {
                //长按
                true
            })
        } else {
            imageButton.hide()
        }
    }

    private fun initPermissions() {
        var rxPermissions = RxPermissions(this)
        //手机信息权限询问
        rxPermissions.request(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe({ granted ->
            if (granted) {
                //do nothing
            } else {
                showToast(R.string.activity_toast_no_permission_tib)
            }
        })
    }


    override fun onBackPressed() {
        var mNowTime = System.currentTimeMillis()
        if ((mNowTime - mPressedTime)>2000){
            showToastLong("再按一次退出程序")
            mPressedTime = mNowTime
        }
        else{
            //退出程序
            finish()
            System.exit(0)
        }
    }
}

