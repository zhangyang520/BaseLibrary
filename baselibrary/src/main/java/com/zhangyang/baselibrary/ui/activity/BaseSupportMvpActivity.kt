package com.zhangyang.baselibrary.ui.activity

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
 *
 *  基础的 支持 supprotActivity的mvp 框架
 *  @Title ${name}
 *  @ProjectName ZCodeAssets
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/1718:41
 *
 */
abstract class BaseSupportMvpActivity<T: BasePresenter<*>>:BaseSupportActivity(),BaseView {

    protected lateinit  var activityComponent: ActivityComponent;

    var hud2: KProgressHUD?=null
    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        initAcitivityInjection();
        injectCompontent()
        super.onCreate(savedInstanceState)
    }

    private fun initAcitivityInjection() {
        activityComponent= DaggerActivityComponent.builder().
                activityModule(ActivityModule(this)).
                lifecylerProviderModule(LifecylerProviderModule(this)).
                appComponent((applicationContext as BaseApplication).appComponent).build();
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
        ToastUtil.makeText(this,error);
    }

    /**
     * 登录失败的反馈
     */
    override fun onLoginError(error: String, code: String) {
        outLoginState(this,error);
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}