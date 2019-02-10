package com.zhangyang.baselibrary.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 *
 *  检查网络的工具
 *  @Title ${name}
 *  @ProjectName ZCodeAssets
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/1916:41
 *
 */
object NetWorkUtils {

    public fun isNetworkConnected(context: Context):Boolean {
        if (context != null) {
            var mConnectivityManager =context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as  (ConnectivityManager)
            var mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public fun isWifiConnected( context:Context):Boolean {
        if (context != null) {
                var mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
                var  mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWiFiNetworkInfo != null) {
                    return mWiFiNetworkInfo.isAvailable();
                }
        }
        return false;
    }


    public fun isMobileConnected( context:Context):Boolean {
        if (context != null) {
            var  mConnectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager)
            var  mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
    }
        return false;
    }


    public  fun getConnectedType(context:Context):Int {
        if (context != null) {
            var  mConnectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager)
            var  mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

}