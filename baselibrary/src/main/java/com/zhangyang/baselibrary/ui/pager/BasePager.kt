package com.zhangyang.baselibrary.ui.pager

import android.content.Context
import android.view.View
import com.zhangyang.baselibrary.common.AppManager
import com.zhangyang.baselibrary.common.BaseApplication
import com.zhangyang.baselibrary.common.BaseApplication.Companion.context
import com.zhangyang.baselibrary.data.dao.UserDao
import com.zhangyang.baselibrary.injection.component.ActivityComponent
import com.zhangyang.baselibrary.injection.module.ActivityModule
import com.zhangyang.baselibrary.injection.module.LifecylerProviderModule
import com.zhangyang.baselibrary.presenter.BasePresenter
import com.zhangyang.baselibrary.ui.activity.BaseAcvitiy
import com.zhangyang.baselibrary.ui.dialog.LuyinTipDialog
import com.zhangyang.baselibrary.ui.progressbar.KProgressHUD
import javax.inject.Inject
import com.zhangyang.baselibrary.R
import com.zhangyang.baselibrary.injection.component.DaggerActivityComponent

/**
 *   基础pager的类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/7/10 16:07
 */
abstract class BasePager<T: BasePresenter<*>>: PagerLifecyclerProvider(){


    @Inject
    lateinit var basePresenter: T

    var activityComponent: ActivityComponent?=null

    var khProgressBar: KProgressHUD?=null
    /**
     * 初始化
     */
   fun  initPager(context: Context){
        //初始化activity
        injectActivityComponent(context)
        initComponent()
   }

    abstract fun initComponent()

    fun injectActivityComponent(context: Context) {
        activityComponent= DaggerActivityComponent.builder().
                lifecylerProviderModule(LifecylerProviderModule(this))
                .activityModule(ActivityModule(context as BaseAcvitiy))
                .appComponent(((context as BaseAcvitiy).applicationContext as BaseApplication).appComponent).build();
    }


    /**
     * 展示对话框
     */
    open fun showLoading(){
        if(khProgressBar==null){
            khProgressBar=KProgressHUD.create(context, KProgressHUD.Style.DOU_DONG).setCancellable(false);
            khProgressBar!!.show()
        }else if(!!khProgressBar!!.isShowing){
            khProgressBar!!.show();
        }
    }

    /**
     * 隐藏对话框
     */
    open fun hideLoading(){
        if(khProgressBar!=null && khProgressBar!!.isShowing){
            khProgressBar!!.dismiss()
        }
    }

    /**
     * 展示错误
     */
    open fun onError(error:String,vararg args:String){

    }

    /**
     * 未登录
     */
    open fun onLoginError(error:String,code:String){
        outLoginState(context,error)
    }

    /**
     * 用户未登录时，提示弹窗并退出登录
     *
     * @param context 当前所在的活动类
     * @param popmsg  弹窗的提示信息
     */
    var luyinTipDialog: LuyinTipDialog? = null

    fun outLoginState(context: Context, popmsg: String) {
        //更新保存本地的所有用户的登录状态，为未登录
        UserDao.updateAllUserLocalState(false)
        //未登录的情形: 业务的处理
        //ToastUtil.makeText(this,Constants.NO_LOGIN_EXCEPTION);
        try {
            if (luyinTipDialog == null) {
                luyinTipDialog = LuyinTipDialog(context, R.style.simulationDialog, popmsg)
                luyinTipDialog!!.setListener(object : LuyinTipDialog.CustomDialogListener{
                    override fun onClick(view: View) {
                        //退出时，友盟统计记录账号退出
                        luyinTipDialog!!.dismiss()
                        //销毁所有Activity，并跳转至登录页面
                        AppManager.instance.outLogin(context)
                    }
                })
            }
            if (!luyinTipDialog!!.isShowing()) {
                luyinTipDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}