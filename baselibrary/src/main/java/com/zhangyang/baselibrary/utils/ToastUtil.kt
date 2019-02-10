package com.zhangyang.baselibrary.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.zhangyang.baselibrary.common.BaseConstants


/**
 * <提示公共类>
 *
 * @author wangzhenqi
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 *
 * @since [V1]
</提示公共类> */
object ToastUtil {
    private var toast: Toast? = null

    /**
     * <显示toast提示>短2S提示
     *
     * @param context
     * @param msg
     * @see [类、类.方法、类.成员]
    </显示toast提示> */
    fun makeText(context: Context, msg: String) {
        //        if (AndroidApplication.currentActivityName.equals(context.getClass().getName())) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
        //        }
    }

    /**
     * <显示toast提示>长3.5S提示
     *
     * @param context
     * @param msg
     * @see [类、类.方法、类.成员]
    </显示toast提示> */
    fun makeTextLong(context: Context, msg: String) {
        //        if (AndroidApplication.currentActivityName.equals(context.getClass().getName())) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
        //        }
    }

    /**
     * <显示toast提示>短2S提示-中间位置
     *
     * @param context
     * @param msg
     * @see [类、类.方法、类.成员]
    </显示toast提示> */
    fun makeTextCenter(context: Context, msg: String) {
        //        if (currentActivityName.equals(context.getClass().getName())) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
        //        }
    }

    /**
     * <显示toast提示>长3.5S提示-中间位置
     *
     * @param context
     * @param msg
     * @see [类、类.方法、类.成员]
    </显示toast提示> */
    fun makeTextLongCenter(context: Context, msg: String) {
        //        if (AndroidApplication.currentActivityName.equals(context.getClass().getName())) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
        //        }
    }

    /**
     * <显示失败信息>
     *
     * @param context
     * @see [类、类.方法、类.成员]
    </显示失败信息> */
    fun makeTextError(context: Context) {
        makeText(context, BaseConstants.ERROR_MESSAGE)
    }

    /**
     * <显示失败信息>
     *
     * @param context
     * @see [类、类.方法、类.成员]
    </显示失败信息> */
    fun makeTextErrorLong(context: Context) {
        makeTextLong(context, BaseConstants.ERROR_MESSAGE)
    }

    fun destory() {
        toast = null
    }
}
