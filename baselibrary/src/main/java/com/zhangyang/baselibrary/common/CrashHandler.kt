package com.zhangyang.baselibrary.common

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.widget.Toast
import com.zhangyang.baselibrary.utils.SDCardUtil
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zhangyang on 2018/5/17 16:51.
 * version 1
 */
class CrashHandler : Thread.UncaughtExceptionHandler {
    val TAG = "CrashHandler"

    //系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    //程序的Context对象
    private var mContext: Context? = null
    //用来存储设备信息和异常信息
    private val infos = HashMap<String, String>()


    /** 保证只有一个CrashHandler实例  */
    private constructor();

    /** 获取CrashHandler实例 ,单例模式  */
    companion object{
        var INSTANCE=CrashHandler();
    }
    fun getInstance(): CrashHandler {
        return INSTANCE
    }

    /**
     * 初始化

     * @param context
     */
    fun init(context: Context) {
        mContext = context
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.

     * @param ex
     * *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        ex.printStackTrace()
        //使用Toast来显示异常信息
        infos.put("error-path", ex.message!!)
        //收集设备参数信息
        collectDeviceInfo(mContext!!)
        //保存日志文件
        saveCrashInfo2File(ex)
        object : Thread() {
            override fun run() {
                Looper.prepare()
                ex.printStackTrace()////Log.e("", "", ex);
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show()
                Looper.loop()
            }
        }.start()

        return true
    }

    /**
     * 收集设备参数信息
     * @param ctx
     */
    fun collectDeviceInfo(ctx: Context) {
        try {
            val pm = ctx.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                infos.put("versionName", versionName)
                infos.put("versionCode", versionCode)
                val calendar = Calendar.getInstance()
                val formate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                infos.put("currentTime", formate.format(calendar.time))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos.put(field.name, field.get(null).toString())
                //                HLog.getInstance().d(TAG, field.getName() + " : " + field.get(null));
            } catch (e: Exception) {
                e.printStackTrace()
                //                HLog.getInstance().e(TAG,e.getLocalizedMessage());
            }

        }
    }

    /**
     * 保存错误信息到文件中

     * @param ex
     * *
     * @return  返回文件名称,便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable) {
        val sb = StringBuffer()
        for ((key, value) in infos) {
            sb.append(key + "=" + value + "\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        try {
            //if (BaseApplication.hasSDcard()){
            val dir = SDCardUtil.instance.getDebugPath()
            val fileDirectroy = File(dir)
            if (!fileDirectroy.exists()) {
                fileDirectroy.mkdir()
            }
            val fire = File(fileDirectroy, "log.txt")
            if (!fire.exists()) fire.createNewFile()
            if (fire.length() > 1024 * 1024 * 50) {//大于50M则自动删除
                fire.delete()
                fire.createNewFile()
            }
            val fos = FileOutputStream(fire, true)
            fos.write(sb.toString().toByteArray())
            fos.write("\n-----------------------\n".toByteArray())
            fos.flush()
            fos.close()
            //}
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * 保存错误信息到文件中

     * @param actionLog
     * *
     * @return  返回文件名称,便于将文件传送到服务器
     */
    fun saveActionLog2File(actionLog: String) {
        val sb = StringBuffer()
        val formate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sb.append(formate.format(Calendar.getInstance().time) + ":" + actionLog)
        try {
            //if (BaseApplication.hasSDcard()){
            val dir =SDCardUtil.instance.getDebugPath()
            val fileDirectroy = File(dir)
            if (!fileDirectroy.exists()) {
                fileDirectroy.mkdir()
            }
            val fire = File(fileDirectroy, "log.txt")
            if (!fire.exists()) fire.createNewFile()
            if (fire.length() > 1024 * 1024 * 50) {//大于50M则自动删除
                fire.delete()
                fire.createNewFile()
            }
            val fos = FileOutputStream(fire, true)
            fos.write(sb.toString().toByteArray())
            fos.write("\n-----------------------\n".toByteArray())
            fos.flush()
            fos.close()
            //}
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    fun saveUmengTongjiLog2File(actionLog: String) {
        val sb = StringBuffer()
        val formate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sb.append(formate.format(Calendar.getInstance().time) + ":" + actionLog)
        try {
            //if (BaseApplication.hasSDcard()){
            val dir = SDCardUtil.instance.getDebugPath()
            val fileDirectroy = File(dir)
            if (!fileDirectroy.exists()) {
                fileDirectroy.mkdir()
            }
            val fire = File(fileDirectroy, "chiShengluyin.txt")
            if (!fire.exists()) fire.createNewFile()
            if (fire.length() > 1024 * 1024 * 50) {//大于50M则自动删除
                fire.delete()
                fire.createNewFile()
            }
            val fos = FileOutputStream(fire, true)
            fos.write(sb.toString().toByteArray())
            fos.write("\n-----------------------\n".toByteArray())
            fos.flush()
            fos.close()
            //}
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }
}