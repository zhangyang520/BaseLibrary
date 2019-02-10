package com.zhangyang.baselibrary.ui.activity

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhangyang.baselibrary.R
import com.zhangyang.baselibrary.common.AppManager
import com.zhangyang.baselibrary.ui.dialog.LuyinTipDialog
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.layout_title.*
import me.yokeyword.fragmentation.*
import me.yokeyword.fragmentation.anim.FragmentAnimator
import java.util.HashMap

/**
 *  @Title ${name}
 *  @ProjectName ZCodeAssets
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/1718:37
 *
 */
abstract class BaseSupportActivity:RxAppCompatActivity(), ISupportActivity {

    internal val mDelegate = SupportActivityDelegate(this)


    lateinit protected var root: View//根view
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



    override fun onResume() {
        super.onResume()
        AppManager.instance.addActivity(this)
    }

    /**
     * 进行初始化black和主题的业务
     */
    fun initBtnBlackAndTitle() {
        if (isBlackShow) {
            val black = root.findViewById<RelativeLayout>(R.id.rl_back)
            black.visibility = View.VISIBLE
            black.setOnClickListener {
                beforeFinish()
            }
        }

        if (isTitleShow) {
            //进行设置主题
            val tv_Title = root.findViewById<TextView>(R.id.tv_title)
            tv_Title.text = viewtitle
        }

        if(isRightShow){
            val tv_Title = root.findViewById<TextView>(R.id.tv_right)
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
    abstract fun getView(): View

    /**
     * 处理对应的业务
     */
    open fun processBussiness(){

    }
    /**
     * 点击返回键 以及箭头之前调用的方法
     */
    open fun beforeFinish() {
        finish()
    }

    open fun setRight(rightTitle: String) {
        if(isRightShow){
            this.rightTitle=rightTitle
            tv_right.text=rightTitle
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
            val checkCallPhonePermission = ContextCompat.checkSelfPermission(applicationContext, permission)
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                ActivityCompat.requestPermissions(this, arrayOf(permission), id)
                return
            } else {
                allowableRunnable?.run()
            }
        } else {
            val result = PermissionChecker.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED
            if (!result) {
                //如果未授权
                ActivityCompat.requestPermissions(this, arrayOf(permission), id)
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


    override fun getSupportDelegate(): SupportActivityDelegate {
        return mDelegate
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    override fun extraTransaction(): ExtraTransaction {
        return mDelegate.extraTransaction()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root=getView();
        setContentView(root)
        initView();
        processBussiness()
        //进行初始化back 和主题
        initBtnBlackAndTitle()
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDelegate.onPostCreate(savedInstanceState)
    }

    override fun onDestroy() {
        mDelegate.onDestroy()
        AppManager.instance.removeActivity(this)
        super.onDestroy()
    }

    /**
     * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

    /**
     * 不建议复写该方法,请使用 [.onBackPressedSupport] 代替
     */
    override fun onBackPressed() {
        mDelegate.onBackPressed()
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    override fun onBackPressedSupport() {
        mDelegate.onBackPressedSupport()
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    override fun getFragmentAnimator(): FragmentAnimator {
        return mDelegate.fragmentAnimator
    }

    /**
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    /**
     * Set all fragments animation.
     * 构建Fragment转场动画
     *
     *
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return mDelegate.onCreateFragmentAnimator()
    }

    override fun post(runnable: Runnable) {
        mDelegate.post(runnable)
    }

    /****************************************以下为可选方法(Optional methods)******************************************************/

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    fun loadRootFragment(containerId: Int, toFragment: ISupportFragment) {
        mDelegate.loadRootFragment(containerId, toFragment)
    }

    fun loadRootFragment(containerId: Int, toFragment: ISupportFragment, addToBackStack: Boolean, allowAnimation: Boolean) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnimation)
    }

    /**
     * 加载多个同级根Fragment,类似Wechat, QQ主页的场景
     */
    fun loadMultipleRootFragment(containerId: Int, showPosition: Int, vararg toFragments: ISupportFragment) {
        mDelegate.loadMultipleRootFragment(containerId, showPosition, *toFragments)
    }

    /**
     * show一个Fragment,hide其他同栈所有Fragment
     * 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     *
     *
     * 建议使用更明确的[.showHideFragment]
     *
     * @param showFragment 需要show的Fragment
     */
    fun showHideFragment(showFragment: ISupportFragment) {
        mDelegate.showHideFragment(showFragment)
    }

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     */
    fun showHideFragment(showFragment: ISupportFragment, hideFragment: ISupportFragment) {
        mDelegate.showHideFragment(showFragment, hideFragment)
    }

    /**
     * It is recommended to use [SupportFragment.start].
     */
    fun start(toFragment: ISupportFragment) {
        mDelegate.start(toFragment)
    }

    /**
     * It is recommended to use [SupportFragment.start].
     *
     * @param launchMode Similar to Activity's LaunchMode.
     */
    fun start(toFragment: ISupportFragment, @ISupportFragment.LaunchMode launchMode: Int) {
        mDelegate.start(toFragment, launchMode)
    }

    /**
     * It is recommended to use [SupportFragment.startForResult].
     * Launch an fragment for which you would like a result when it poped.
     */
    fun startForResult(toFragment: ISupportFragment, requestCode: Int) {
        mDelegate.startForResult(toFragment, requestCode)
    }

    /**
     * It is recommended to use [SupportFragment.startWithPop].
     * Start the target Fragment and pop itself
     */
    fun startWithPop(toFragment: ISupportFragment) {
        mDelegate.startWithPop(toFragment)
    }

    /**
     * It is recommended to use [SupportFragment.startWithPopTo].
     *
     * @see .popTo
     * @see .start
     */
    fun startWithPopTo(toFragment: ISupportFragment, targetFragmentClass: Class<*>, includeTargetFragment: Boolean) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment)
    }

    /**
     * It is recommended to use [SupportFragment.replaceFragment].
     */
    fun replaceFragment(toFragment: ISupportFragment, addToBackStack: Boolean) {
        mDelegate.replaceFragment(toFragment, addToBackStack)
    }

    /**
     * Pop the fragment.
     */
    fun pop() {
        mDelegate.pop()
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     *
     *
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    fun popTo(targetFragmentClass: Class<*>, includeTargetFragment: Boolean) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment)
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    fun popTo(targetFragmentClass: Class<*>, includeTargetFragment: Boolean, afterPopTransactionRunnable: Runnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable)
    }

    fun popTo(targetFragmentClass: Class<*>, includeTargetFragment: Boolean, afterPopTransactionRunnable: Runnable, popAnim: Int) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim)
    }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * Fragmentation默认使用Theme的android:windowbackground作为Fragment的背景,
     * 可以通过该方法改变其内所有Fragment的默认背景。
     */
    fun setDefaultFragmentBackground(@DrawableRes backgroundRes: Int) {
        mDelegate.defaultFragmentBackground = backgroundRes
    }

    /**
     * 得到位于栈顶Fragment
     */
    fun getTopFragment(): ISupportFragment {
        return SupportHelper.getTopFragment(supportFragmentManager)
    }

    /**
     * 获取栈内的fragment对象
     */
    fun <T : ISupportFragment> findFragment(fragmentClass: Class<T>): T {
        return SupportHelper.findFragment(supportFragmentManager, fragmentClass)
    }
}