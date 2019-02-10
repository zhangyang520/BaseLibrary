package com.zhangyang.baselibrary.Rx

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.zhangyang.baselibrary.common.BaseConstants
import com.zhangyang.baselibrary.data.bean.BaseResp
import com.zhangyang.baselibrary.exception.ContentException
import com.zhangyang.baselibrary.utils.SystemUtil
import com.trello.rxlifecycle.LifecycleProvider
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1

/**
 * Created by ASUS on 2018/6/1.
 * 主要拓展 某个类的方法
 */


/**
 * 将公共的执行 执行功能
 * LifecycleProvider
 */
fun  <T>Observable<T>.execute(subscribe:Subscriber<T>,lifecylerProvider: LifecycleProvider<*>) {
    this.subscribeOn(rx.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(lifecylerProvider.bindToLifecycle())
            .subscribe(subscribe)

}

/**
 * 拓展Observable的flatMap方法<T>
 */
fun <T>Observable<BaseResp<T>>.convertData():Observable<T>{
    SystemUtil.printlnStr("BaseSucriber convertData....:")
    return this.flatMap(object :Func1<BaseResp<T>,Observable<T>>{
        override fun call(t: BaseResp<T>?): Observable<T> {
            SystemUtil.printlnStr("BaseSucriber call....:"+t.toString())
            if(!t!!.status.equals(BaseConstants.SUCCESS)){
                SystemUtil.printlnStr("BaseSucriber call failure")
                return rx.Observable.error(ContentException(t.message))
            }
            SystemUtil.printlnStr("BaseSucriber call success")
            return rx.Observable.just(t!!.data as T)
        }
    })
}

private fun View.onBtnClick(listener: View.OnClickListener) {
    this.setOnClickListener(listener)
}

 fun EditText.hasTxt():Boolean{
    return this.text.toString().trim().length>0
}

fun TextView.hasTxt():Boolean{
    return this.text.toString().trim().length>0
}

fun  TextView.getTxt():String{
    return this.text.toString()
}

fun  EditText.getTxt():String{
    return this.text.toString()
}
/**
 * 拓展Observable的flatMap的<Boolean> 方法
 */
fun <T>Observable<BaseResp<T>>.convertBoolean():Observable<Boolean>{
    return this.flatMap(object:Func1<BaseResp<T>,Observable<Boolean>>{
        override fun call(t: BaseResp<T>?): Observable<Boolean> {
            if(t!!.status.equals(BaseConstants.SUCCESS)){
                return rx.Observable.error(ContentException(t.message))
            }
            return rx.Observable.just(true);
        }
    })
}

/**
 * 增加一个断言
 */
fun <T> assertMethod(t:T,method:()->Unit){
    if(t!=null){
        method()
    }
}

/**
 *  根据一次 flag 是否执行 method
 */
fun assertBooleanMethod(flag:Boolean,method:()->Unit){
    if(flag){
        method()
    }
}