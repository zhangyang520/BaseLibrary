package com.zhangyang.baselibrary.presenter.view

/**
 * Created by ASUS on 2018/5/25.
 * 基础的业务view
 */
interface BaseView {
    /**
     * 展示对话框
     */
    fun showLoading(){

    }

    /**
     * 隐藏对话框
     */
    fun hideLoading(){

    }

    /**
     * 展示错误
     */
    fun onError(error:String,vararg args:String){

    }

    /**
     *
     */
    fun onLoginError(error:String,code:String){

    }
}