package com.suntray.chinapost.baselibrary.ui.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by ASUS on 2017/9/26.
 */
class HomeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action
        if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)

            if (SYSTEM_DIALOG_REASON_HOME_KEY == reason) {
                // 短按Home键
                //处于非 四个界面 四级两人互动,四级全真模拟,六级讨论,六级全真模拟
                //                if(context instanceof BaseActivity){
                //                    ((BaseActivity)context).setHomeExistFlag(true);
                //                    if(!(context instanceof ReallyTestSimulationActivity || context instanceof GroupInteractionCombatActivity ||
                //                            context instanceof SixSimulationActivity || context instanceof InteractionCombatActivity) && BaseActivity.IsLearnAndWait && context instanceof BaseActivity){
                //                        //并且处于边学边等模式
                //                        GroupCombatPairPager.getIntroducePager().existApplyMatch();//进行退出匹配队列
                //                    }
                //                }
            } else if (SYSTEM_DIALOG_REASON_RECENT_APPS == reason) {
                // 长按Home键 或者 activity切换键
            } else if (SYSTEM_DIALOG_REASON_LOCK == reason) {
                // 锁屏
                //System.out.println("HomeReceiver 锁屏");
            } else if (SYSTEM_DIALOG_REASON_ASSIST == reason) {
                // samsung 长按Home键
            }
        }
    }

    /**
     * 常量定义
     */
    companion object {
        private val SYSTEM_DIALOG_REASON_KEY = "reason"
        private val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
        private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
        private val SYSTEM_DIALOG_REASON_LOCK = "lock"
        private val SYSTEM_DIALOG_REASON_ASSIST = "assist"
    }
}
