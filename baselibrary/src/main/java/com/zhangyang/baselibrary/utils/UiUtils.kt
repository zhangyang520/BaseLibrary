package com.jianhua.zcode.assets.baselibrary

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.util.DisplayMetrics
import com.zhangyang.baselibrary.common.BaseApplication


/**
 * Created by zhangyang on 2018/5/17 17:17.
 * version 1
 */
class UiUtils {

    /**
     * 私有化构造函数
     */
    private constructor()

    companion object{
        val instance=UiUtils();
    }
    /**
     * 进行获取资源类

     * @return
     */
    private fun getResources(): Resources {
        return BaseApplication.context.getResources()
    }


    /**
     * 进行获取图片的Drawable

     * @param
     * *
     * @return
     */
    fun getDrawable(drawableId: Int): Drawable {

        return getResources().getDrawable(drawableId)
    }

    fun getDimen(id: Int): Int {
        return (getResources().getDimension(id) + 0.5f).toInt()
    }


    /**
     * 进行填充view对象

     * @param viewId
     * *
     * @return
     */
    fun inflate(viewId: Int): View {
        ////System.out.println("UiUtils...getContext() = " + (getContext() == null));
        return View.inflate(BaseApplication.context, viewId, null)
    }


    /**
     * 进行实现runOnUiThread

     * @param runnable
     */
    fun runOnUiThread(runnable: Runnable) {

        // 首先进行判断是不是主线程
        if (android.os.Process.myTid() == //
                (BaseApplication.context as BaseApplication).tid) {
            // 如果是主线程,进行运行runnable
            runnable.run()
        } else {
            // 如果不是主线程,进行将方法进行提交都主线程
            BaseApplication.handler.post(runnable)
        }
    }

    /**
     * 进行提供获取Context

     * @return
     */
    fun getContext(): Context {
        return BaseApplication.context
    }

    /**
     * 进行获取Handler
     */
    fun getHandler(): Handler {
        return BaseApplication.handler
    }

    /**
     * 进行显示信息

     * @param msg
     */
    fun showToast(msg: String) {
        runOnUiThread(Runnable { Toast.makeText(BaseApplication.context, msg, Toast.LENGTH_SHORT).show() })
    }

    /**
     * 进行获取屏幕的宽度
     * @return
     */
    fun getDefaultWidth(): Int {
        return (BaseApplication.context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.width
    }

    /**
     * 进行获取屏幕的高度

     * @return
     */
    fun getDefaultHeight(): Int {
        return (BaseApplication.context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.height
    }

    /**
     * 进行获取GET_META_DATA的ApplicationInfo
     * @param packageManager
     * *
     * @param packageName
     * *
     * @return
     */
    private fun getAppMetaDataBundle(packageManager: PackageManager,
                                     packageName: String): Bundle? {
        var bundle: Bundle? = null
        try {
            val ai = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA)
            bundle = ai.metaData
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return bundle
    }

    /**
     * 进行获取对应的键的值
     * @param key
     * *
     * @param defValue
     * *
     * @param packageName
     * *
     * @return
     */
    fun getMetaDataStringApplication(key: String, defValue: String, packageName: String): String {
        val bundle = getAppMetaDataBundle(BaseApplication.context.getPackageManager(), packageName)
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key)
        }
        return defValue
    }

    /**
     * 把图片的比例传值进来，再测量屏幕的宽度，得出高度值
     * @param context
     * @param width
     * @param height
     * @return
     */
    fun getHeight(context: Context, width: Int, height: Int): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val scressWidth = windowManager.defaultDisplay.width
        return if (width != 0) {
            (height * scressWidth / width.toFloat()).toInt()
        } else 0
    }

    /**
     * 进行获取屏幕宽度
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay.width
    }

    /**
     * 进行虎丘屏幕宽度
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay.height
    }

    fun getDensity(context: Context): Float {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        //		//System.out.println("screen showDensity:"+displayMetrics.density+"...density dpi:"+displayMetrics.densityDpi);
        return displayMetrics.density
    }

    /**
     * dp转换成像素
     * @param context
     * @param dp
     * @return
     */
    fun dp2Px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    /**
     * 像素转换成dp
     * @param context
     * @param px
     * @return
     */
    fun px2Dp(context: Context, px: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }
}