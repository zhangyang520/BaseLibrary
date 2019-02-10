package com.zhangyang.baselibrary.utils

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import com.zhangyang.baselibrary.R
import com.zhangyang.baselibrary.common.BaseConstants
import com.zhangyang.baselibrary.exception.ContentException
import com.zhangyang.baselibrary.ui.dialog.LuyinTipDialog


/**
 * Created by zhangyang on 2018/5/18 10:55.
 * version 1
 */
object  SystemUtil {
    /**
     * 获取当前进程名
     * @param context
     * *
     * @return 进程名
     */
    fun getProcessName(context: Context): String {
        var processName: String? = null

        // ActivityManager
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        while (true) {
            for (info in am.runningAppProcesses) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName
                    break
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName!!
            }

            // take a rest and again
            try {
                Thread.sleep(100L)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }

        }
    }

    /**
     * 获取版本号，返回获取的当前版本号

     * @return
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getVersionName(context: Context): String {
        // 获取packagemanager的实例
        val packageManager = context.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        val packInfo = packageManager.getPackageInfo(context.packageName, 0)
        val version = packInfo.versionName
        return version
    }

    fun isWeixinAvilible(context: Context): Boolean {
        val packageManager = context.packageManager// 获取packagemanager
        val pinfo = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                if (pn == "com.tencent.mm") {
                    return true
                }
            }
        }

        return false
    }

    /**
     * 是否比服务器端传递过来的apk的版本号大
     * @param jsonApkValue
     * *
     * @return
     * *
     * @throws
     */
    @Throws(ContentException::class)
    fun isLowerJsonApk(localApkVersion: String, jsonApkValue: String): Boolean {
        try {
            //进行获取本地的版本号
            //先进行判断版本的格式
            val regex = "^[0-9]+(.[0-9]+)*$"
            if (!jsonApkValue.matches(regex.toRegex())) {
                throw ContentException("软件版本号获取失败！")
            } else if (!localApkVersion.matches(regex.toRegex())) {
                throw ContentException("软件版本号获取失败！")
            }

            //进行解析正确格式的版本
            val jsons = jsonApkValue.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val locals = localApkVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val indexLength = if (jsons.size > locals.size) jsons.size else locals.size
            //			for(int ){
            //
            //			}
            ////System.out.println("jsons[] length:"+jsons.length+"....locals[] length:"+locals.length);
            for (i in jsons.indices) {
                //如果其中的某段服务器格式版本>本地的
                if (i <= locals.size - 1) {
                    //进行比对范围之内的
                    if (Integer.valueOf(jsons[i]) > Integer.valueOf(locals[i])) {
                        return true
                    } else if (Integer.valueOf(jsons[i]) < Integer.valueOf(locals[i])) {
                        //如果服务器版本号的长度>本地的，而且之前的比较没有胜负！返回服务器的版本号大
                        return false
                    }
                } else {
                    ////System.out.println("Integer.valueOf(jsons["+i+"]):"+jsons[i]);
                    if (Integer.valueOf(jsons[i]) > 0) {
                        return true
                    } else if (i == jsons.size - 1) {
                        return false
                    }
                }
            }
        } catch (e: NumberFormatException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        //最后返回本地的版本号大 或者相等
        return false
    }

    /**
     * 判断qq是否可用

     * @param context
     * *
     * @return
     */
    fun isQQClientAvailable(context: Context): Boolean {
        val packageManager = context.packageManager
        val pinfo = packageManager.getInstalledPackages(0)
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                if (pn == "com.tencent.mobileqq") {
                    return true
                }
            }
        }
        return false
    }

     lateinit  var alertDialog: AlertDialog
        /**
     * 打印相关的值
     * @param content
     */
    fun printlnStr(content: String) {
        //如果 当前的值打印的值>= 缺省的打印的值
        if (Integer.parseInt(BaseConstants.CURRENT_PRINT_VALUE, 16) > Integer.parseInt(BaseConstants.DEFALUT_PRINT_VALUE, 16)) {
            println(content)
        }
    }

    /**
     * 提示弹窗并执行相关的业务
     *
     * @param context 当前所在的活动类
     * @param popmsg  弹窗的提示信息
     */
    fun alertDialog(context: Context, popmsg: String, listener: LuyinTipDialog.CustomDialogListener): LuyinTipDialog {
        val luyinTipDialog = LuyinTipDialog(context, R.style.simulationDialog, popmsg)
        luyinTipDialog.setListener(listener)
        if (!luyinTipDialog.isShowing) {
            luyinTipDialog.show()
        }
        return luyinTipDialog
    }
}