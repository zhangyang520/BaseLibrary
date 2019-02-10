package com.zhangyang.baselibrary.common

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Debug
import android.os.Handler
import android.preference.PreferenceManager
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.zhangyang.baselibrary.data.bean.User
import com.zhangyang.baselibrary.injection.component.AppComponent
import com.zhangyang.baselibrary.injection.component.DaggerAppComponent
import com.zhangyang.baselibrary.injection.module.AppModule
import com.zhangyang.baselibrary.utils.SystemUtil
import com.lidroid.xutils.DbUtils
import com.lidroid.xutils.exception.DbException


/**
 * Created by zhangyang on 2018/5/17 16:09.
 * 定义一个基础的业务类
 * 有些是其他模块 的独有的一些类 就不在这定义，
 * 放入到App对应的application这个类中定义
 *
 * version 1
 */
abstract  class BaseApplication: MultiDexApplication(){

    companion object{
        lateinit var context:BaseApplication;
        lateinit var sp:SharedPreferences
        lateinit var dbUtils:DbUtils;
        lateinit var handler:Handler;
    }

    public var tid:Int=-1;
     lateinit var appComponent: AppComponent
    /**
     * 创建时候调用
     */
    override fun onCreate() {
        MultiDex.install(this)
        super.onCreate()
        context=this;
        sp=PreferenceManager.getDefaultSharedPreferences(context);
        tid=android.os.Process.myTid();
        //
        handler=Handler();
        //初始化initCrashHander
        initCrashHandler();

        initDataBase();

        initUmengConfig()

        initApplicationInjection();

        ARouter.openLog()    // 打印日志
        ARouter.openDebug()
        ARouter.init(this)
    }


    private fun initApplicationInjection() {
        appComponent= DaggerAppComponent.builder().appModule(AppModule(this)).build();
    }

    /**
     * 初始化友盟的设置
     */
    fun initUmengConfig(){
        try {
            //动态地设置 变量
            val BASE_URL = packageManager.getApplicationInfo(packageName, //
                    PackageManager.GET_META_DATA).metaData.getString("CONSTANT_URL")
            BaseConstants.BASE_URL = BASE_URL

            //获取动态的打印值
            val CURRENT_PRINT = packageManager.getApplicationInfo(packageName, //
                    PackageManager.GET_META_DATA).metaData.getString("CURRENT_PRINT")
            BaseConstants.CURRENT_PRINT_VALUE = CURRENT_PRINT

            val BASE_UPLOAD_URL = packageManager.getApplicationInfo(packageName, //
                    PackageManager.GET_META_DATA).metaData.getString("CONSTANT_UPLOAD_URL")
            BaseConstants.BASE_UPLOAD_URL = BASE_UPLOAD_URL

            var BASE_DOWNLOAD_URL=packageManager.getApplicationInfo(packageName, //
                    PackageManager.GET_META_DATA).metaData.getString("CONSTANT_DOWNLOAD_URL")
            BaseConstants.BASE_DOWNLOAD_URL=BASE_DOWNLOAD_URL
            SystemUtil.printlnStr("BASE_URL:" + BaseConstants.BASE_URL  + "...CURRENT_PRINT_VALUE:" + CURRENT_PRINT+"..BASE_UPLOAD_URL:"+BASE_UPLOAD_URL+"..BASE_DOWNLOAD_URL:"+BASE_DOWNLOAD_URL)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化 數據庫
     */
    fun initDataBase(){
            var daoConfig=DbUtils.DaoConfig(context);
            daoConfig.dbName="ispeak.db"
            daoConfig.dbVersion=BaseConstants.DB_VERSION
            daoConfig.setDbUpgradeListener(object:DbUtils.DbUpgradeListener{
                override fun onUpgrade(p0: DbUtils?, i: Int, i1: Int) {
                    //暫時不加
                    if(i1==10){
                        //第一次升级  用户实体中加类型
                        try {
                            p0!!.execNonQuery("alter table com_suntray_chinapost_baselibrary_data_bean_User add userRole integer")
                        } catch (e: DbException) {
                            e.printStackTrace()
                            //如果数据更新失败，则进行系统退出
                        }
                    }else if(i==10 && i1==11){
                        //orgId
                        //第一次升级  用户实体中加类型
                        try {
                            p0!!.execNonQuery("alter table com_suntray_chinapost_baselibrary_data_bean_User add userRole integer")
                            p0!!.execNonQuery("alter table com_suntray_chinapost_baselibrary_data_bean_User add orgId text")
                        } catch (e: DbException) {
                            e.printStackTrace()
                            //如果数据更新失败，则进行系统退出
                        }
                    } else if(i1==11){
                        //orgId
                        //第一次升级  用户实体中加类型
                        try {
                            p0!!.execNonQuery("alter table com_suntray_chinapost_baselibrary_data_bean_User add orgId text")
                        } catch (e: DbException) {
                            e.printStackTrace()
                            //如果数据更新失败，则进行系统退出
                        }
                    }
                }
            })
        //创建 dbUtils
        dbUtils= DbUtils.create(daoConfig)
        dbUtils.createTableIfNotExist(User::class.java)
        dbUtils.configDebug(true)
    }

    /**
     * 初始化 錯誤日誌的處理
     */
    fun initCrashHandler(){
        var crashHandler=CrashHandler.INSTANCE;
        crashHandler.init(context);
        Debug.stopMethodTracing();
    }
}