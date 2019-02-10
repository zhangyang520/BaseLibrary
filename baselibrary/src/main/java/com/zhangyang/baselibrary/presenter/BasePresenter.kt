package com.zhangyang.baselibrary.presenter

import android.content.Context
import com.zhangyang.baselibrary.presenter.view.BaseView
import com.trello.rxlifecycle.LifecycleProvider
import javax.inject.Inject

/**
 * Created by ASUS on 2018/5/25.
 */
abstract  class BasePresenter <T: BaseView>  {

    /**
     * 基础的baseView
     */
    lateinit var baseView:T

    @Inject
    lateinit var context:Context

    @Inject
    lateinit var lifecylerProvider:LifecycleProvider<*>
    /**
     * 是否要检测网络
     */
    var checkNetwork:Boolean=false;
}