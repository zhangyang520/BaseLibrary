package com.zhangyang.baselibrary.Rx

import com.zhangyang.baselibrary.exception.NoLoginException
import com.zhangyang.baselibrary.presenter.view.BaseView
import com.zhangyang.baselibrary.utils.SystemUtil
import rx.Subscriber
import java.net.SocketTimeoutException

/**
 * Created by ASUS on 2018/6/4.
baseView.onError("请求超时,请查看网络")
}else{
 * 继承Subcriber
 */
open class BaseSucriber<T>(val baseView: BaseView, val tag:String):Subscriber<T>(){

    override fun onStart() {
        super.onStart()
        SystemUtil.printlnStr(tag+" BaseSucriber onStart ....")
        baseView.showLoading()
    }
    override fun onCompleted() {
        SystemUtil.printlnStr(tag+" BaseSucriber onCompleted ....")
        baseView.hideLoading();
    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        SystemUtil.printlnStr(tag+" BaseSucriber onError ....")
        baseView.hideLoading();
        if(e is SocketTimeoutException){
            baseView.onError("请求超时!")
        }else if(e is NoLoginException){
            baseView.onLoginError(e.msg,e.code)
        }else{
            e!!.printStackTrace()
            baseView.onError("请求失败")
        }
    }

    override fun onNext(t: T) {
        SystemUtil.printlnStr(tag+" BaseSucriber onNext ....")
    }
}