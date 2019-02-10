package com.suntray.chinapost.baselibrary.ut.base.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.zhangyang.baselibrary.common.BaseApplication
import com.zhangyang.baselibrary.common.BaseConstants
import com.zhangyang.baselibrary.data.dao.UserDao
import com.zhangyang.baselibrary.exception.ContentException
import com.zhangyang.baselibrary.ui.activity.BaseMvpActivity

/*
    SP工具类
 */
object AppPrefsUtils {

    private var sp: SharedPreferences = BaseApplication.context.
                            getSharedPreferences(BaseConstants.TABLE_PREFS, Context.MODE_PRIVATE)
    private var ed: Editor

    init {
        ed = sp.edit()
    }

    /*
        Boolean数据
     */
    fun putBoolean(key: String, value: Boolean) {
        ed.putBoolean(key, value)
        ed.commit()
    }

    /*
        默认 false
     */
    fun getBoolean(key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    /*
        String数据
     */
    fun putString(key: String, value: String) {
        ed.putString(key, value)
        ed.commit()
    }

    /*
        默认 ""
     */
    fun getString(key: String): String {
        return sp.getString(key, "")
    }

    /**
     * 获取字符串
     */
    fun getString(key: String,default:String): String {
        return sp.getString(key, default)
    }
    /*
        Int数据
     */
    fun putInt(key: String, value: Int) {
        ed.putInt(key, value)
        ed.commit()
    }

    /*
        默认 0
     */
    fun getInt(key: String): Int {
        return sp.getInt(key, 0)
    }


    /*
       默认 0
    */
    fun getInt(key: String,default:Int): Int {
        return sp.getInt(key, default)
    }
    /*
        Long数据
     */
    fun putLong(key: String, value: Long) {
        ed.putLong(key, value)
        ed.commit()
    }

    /*
        默认 0
     */
    fun getLong(key: String): Long {
        return sp.getLong(key, 0)
    }

    /*
        Set数据
     */
    fun putStringSet(key: String, set: Set<String>) {
        val localSet = getStringSet(key).toMutableSet()
        localSet.addAll(set)
        ed.putStringSet(key, localSet)
        ed.commit()
    }

    /*
        默认空set
     */
    fun getStringSet(key: String): Set<String> {
        val set = setOf<String>()
        return sp.getStringSet(key, set)
    }

    /*
        删除key数据
     */
    fun remove(key: String) {
        ed.remove(key)
        ed.commit()
    }

    /**
     * 将用户相关的
     * userId
     * userToken
     */
    fun putUserInfoSet(userId:String,token:String){
        ed.putString("userId",userId);
        ed.putString("userToken",token);
        ed.commit()
    }


    /**
     * 移除对应用户信息
     */
    fun removeUserInfoSet(){
        ed.remove("userId")
        ed.remove("userToken")
        ed.commit()
    }

    /**
     * 获取用户的信息
     */
    fun getUserInfo(baseActivity: BaseMvpActivity<*>):ArrayList<String>?{
        var userId=sp.getString("userId",null);
        if(userId==null){
            try {
                var user= UserDao.getLocalUser();
                var muteSet= arrayListOf<String>()
                muteSet.add(user.id.toString())
                muteSet.add(user.token!!)
                ed.putString("userId",user.id.toString());
                ed.putString("userToken",user.token!!);
                ed.commit();
                return muteSet;
            } catch (e: ContentException) {
                baseActivity.outLoginState(baseActivity,"请重新登录")
                return null
            }
        }else{
            var token=sp.getString("userToken",null);
            var muteSet= arrayListOf<String>()
            muteSet.add(0,userId)
            muteSet.add(1,token)
            return muteSet;
        }
    }
}
