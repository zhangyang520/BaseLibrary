package com.zhangyang.baselibrary.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*
import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.routes.`ARouter$$Providers$$arouterapi`


/**
 * Created by zhangyang on 2018/5/17 15:12.
 *  app 任务栈的管理
 * version 1
 */
class AppManager {
    //定义常量 不
    // 好再修改
    private val statck: Stack<Activity> = Stack<Activity>()

    companion object{
        val instance=AppManager();
    }

    private constructor(){

    }

    /**
     * 往任务栈中 增加activity
     */
    fun addActivity(activity: Activity) {
        statck.add(activity)
    }

    /**
     * 从任务栈中移除 activity
     */
    fun removeActivity(activity: Activity) {
        statck.remove(activity)
    }

    /**
     * 获取上上个activity
     */
    fun getLastLastActivity():Activity?{
        if(statck!=null && statck.size>=2){
            return statck[statck.size-2]
        }
        return null
    }

    fun getLastActivity():Activity?{
        if(statck!=null && statck.size>=1){
            return statck[statck.size-1]
        }
        return null
    }
    /**
     *  全部退出
     */
    fun finishAll(){
        for(activity in statck){
            activity.finish()
        }
        //清空任务栈
        statck.clear();
    }

    /**
     * 退出当前的app
     */
    fun existApp(context: Context) {
//        finishAll()
//        var activityManager = context.getSystemService(Context.ACTIVITY_SERVICE);
//        (activityManager as ActivityManager).killBackgroundProcesses(context.packageName);
//        System.exit(0);
    }

    /**
     *  跳转 到指定的界面
     */
    fun outLogin(context: Context) {
        for (activity in statck) {
            if (!activity.isFinishing()) {
                activity.finish()
            }
        }
        ARouter.getInstance().build("/usercenter/LoginActivity").navigation(context)
    }
}