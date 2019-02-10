package com.zhangyang.baselibrary.ui.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhangyang.baselibrary.common.AppManager
import com.zhangyang.baselibrary.presenter.BasePresenter
import com.zhangyang.baselibrary.presenter.view.BaseView
import com.zhangyang.baselibrary.ui.dialog.LuyinTipDialog
import com.trello.rxlifecycle.components.support.RxFragment
import com.zhangyang.baselibrary.R
import kotlinx.android.synthetic.main.layout_title.*
import java.util.HashMap

/**
 *  @Title ${name}
 *  @ProjectName ZCodeAssets
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/1510:39
 *
 */
abstract class BaseFragment<T : BasePresenter<*>> : RxFragment(), BaseView {
    var root: View?=null//根view
    var viewtitle: String? = null
    var rightTitle:String?=null
    var isTitleShow = true
    var isBlackShow: Boolean = false
    var isRightShow=false
    /**
     * 权限的允许 map集合
     */
    companion object {
        val allowablePermissionRunnables = HashMap<Int, Runnable>()
        val disallowablePermissionRunnables = HashMap<Int, Runnable>()
    }



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(root==null){
            root=getContentView();
            //进行初始化back 和主题
//            initBtnBlackAndTitle()
        }
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    /**
     * 进行初始化black和主题的业务
     */
    fun initBtnBlackAndTitle() {
        if (isBlackShow) {
            val black = root!!.findViewById<RelativeLayout>(R.id.rl_back)
            black.visibility = View.VISIBLE
            black.setOnClickListener {
                beforeFinish()
            }
        }

        if (isTitleShow) {
            //进行设置主题
            val tv_Title = root!!.findViewById<TextView>(R.id.tv_title)
            tv_Title.text = viewtitle
        }

        if(isRightShow){
            val tv_Title = root!!.findViewById<TextView>(R.id.tv_right)
            tv_Title.visibility= View.VISIBLE
            tv_Title.text = rightTitle
            tv_Title.setOnClickListener({
                //右侧事件的处理
                rightTitleClick()
            })
        }
    }
    /**
     * 初始化view相关的信息
     */
    abstract fun initView()

    /**
     * 获取view
     */
    abstract fun getContentView(): View

    /**
     * 处理对应的业务
     */
    open fun processBussiness(){

    }
    /**
     * 点击返回键 以及箭头之前调用的方法
     */
    open fun beforeFinish() {

    }

    open fun setRight(rightTitle: String) {
        if(isRightShow){
            viewtitle=rightTitle
            tv_right.text=viewtitle
        }
    }
    /**
     * 右边的title的点击事件
     */
    open fun rightTitleClick(){

    }

    /**
     * 请求权限
     *
     * @param id                   请求授权的id 唯一标识即可
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    fun requestPermission(id: Int, permission: String, allowableRunnable: Runnable?, disallowableRunnable: Runnable?) {
        if (allowableRunnable != null) {
            allowablePermissionRunnables[id] = allowableRunnable
        }

        if (disallowableRunnable != null) {
            disallowablePermissionRunnables[id] = disallowableRunnable
        }

        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限checkCallPhonePermission != PackageManager.PERMISSION_GRANTED
            val checkCallPhonePermission = ContextCompat.checkSelfPermission(context, permission)
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                ActivityCompat.requestPermissions(activity, arrayOf(permission), id)
                return
            } else {
                allowableRunnable?.run()
            }
        } else {
            val result = PermissionChecker.checkSelfPermission(activity, permission) == PermissionChecker.PERMISSION_GRANTED
            if (!result) {
                //如果未授权
                ActivityCompat.requestPermissions(activity, arrayOf(permission), id)
            } else {
                allowableRunnable?.run()
            }
        }
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
        //未登录的情形: 业务的处理
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults != null && grantResults.size >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val allowRun = allowablePermissionRunnables[requestCode]
            allowRun?.run()
        } else {
            val disallowRun = disallowablePermissionRunnables[requestCode]
            disallowRun?.run()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}