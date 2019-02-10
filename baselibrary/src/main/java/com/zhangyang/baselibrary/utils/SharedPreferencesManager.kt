package com.zhangyang.baselibrary.utils

/**
 * Created by suntray.chinapost on 2018/5/18 09:45.
 * version 1
 */

import android.content.Context
import android.content.SharedPreferences
import com.zhangyang.baselibrary.common.BaseApplication
import java.lang.reflect.Method

object SharedPreferencesManager {
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * *
     * @param key
     * *
     * @param object
     */
    fun put(context: Context, key: String,obj: Any) {

        val sp = BaseApplication.sp
        val editor = sp.edit()
        if (obj is String) {
            editor.putString(key,obj)
        } else if (obj is Int) {
            editor.putInt(key,obj)
        } else if (obj is Boolean) {
            editor.putBoolean(key,obj)
        } else if (obj is Float) {
            editor.putFloat(key,obj)
        } else if (obj is Long) {
            editor.putLong(key,obj)
        } else {
            editor.putString(key,obj.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值

     * @param context
     * *
     * @param key
     * *
     * @param defaultObject
     * *
     * @return
     */
    operator fun <T>get(context: Context, key: String, defaultObject: T): T{
        val sp = BaseApplication.sp
        if (defaultObject is String) {
            return sp.getString(key, defaultObject) as T
        } else if (defaultObject is Int) {
            return sp.getInt(key, defaultObject)  as T
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, defaultObject)  as T
        } else if (defaultObject is Float) {
            return sp.getFloat(key, defaultObject)  as T
        } else if (defaultObject is Long) {
            return sp.getLong(key, defaultObject)  as T
        }else{
            return defaultObject;
        }
    }

    /**
     * 移除某个key值已经对应的值

     * @param context
     * *
     * @param key
     */
    fun remove(context: Context, key: String) {
        val sp = BaseApplication.sp
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据

     * @param context
     */
    fun clear(context: Context) {
        val sp = BaseApplication.sp
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在

     * @param context
     * *
     * @param key
     * *
     * @return
     */
    fun contains(context: Context, key: String): Boolean {
        val sp = BaseApplication.sp
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对

     * @param context
     * *
     * @return
     */
    fun getAll(context: Context): Map<String, *> {
        val sp = BaseApplication.sp
        return sp.getAll()
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类

     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法

         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }

            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit

         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {

            }
            editor.commit()
        }
    }
}