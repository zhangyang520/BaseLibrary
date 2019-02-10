package com.zhangyang.baselibrary.ui.fragment

import android.os.Bundle
import com.zhangyang.baselibrary.common.BaseApplication
import com.zhangyang.baselibrary.injection.component.ActivityComponent
import com.zhangyang.baselibrary.injection.component.DaggerActivityComponent
import com.zhangyang.baselibrary.injection.module.ActivityModule
import com.zhangyang.baselibrary.injection.module.LifecylerProviderModule
import com.zhangyang.baselibrary.presenter.BasePresenter
import com.zhangyang.baselibrary.presenter.view.BaseView
import com.zhangyang.baselibrary.ui.progressbar.KProgressHUD
import com.zhangyang.baselibrary.utils.ToastUtil

import javax.inject.Inject


/**
 *  Created by ASUS on 2018/5/25.
 *  基础的mvp框架的activity类
 */
 abstract class BaseMvpFragment<T: BasePresenter<*>>: BaseFragment<T>(), BaseView {

    protected lateinit  var activityComponent: ActivityComponent;
    var hud2: KProgressHUD?=null

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAcitivityInjection();
        injectCompontent()
    }

    private fun initAcitivityInjection() {
        activityComponent= DaggerActivityComponent.builder().
                                            activityModule(ActivityModule(activity)).
                                            lifecylerProviderModule(LifecylerProviderModule(this)).
                                            appComponent((activity.applicationContext as BaseApplication).appComponent).build();
    }

    /**
     * 注入component
     */
    abstract fun injectCompontent()

    /**
     * 基础的basePresenter的类
     */
    @Inject
    lateinit var  basePresenter:T


    /**
     * 展示进度条
     */
    override fun showLoading(){
        if(hud2!=null && !hud2!!.isShowing){
            hud2?.show()
        }
   }

    /**
     * 隐藏进度条
     */
    override fun hideLoading(){
        if(hud2!=null && hud2!!.isShowing){
            hud2?.dismiss()
        }
    }

    /**
     * 错误的反馈
     */
    override fun onError(error:String,vararg args:String){
        ToastUtil.makeText(activity,error);
    }

    /**
     * 登录失败的反馈
     */
    override fun onLoginError(error: String, code: String) {
        outLoginState(activity,error);
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}