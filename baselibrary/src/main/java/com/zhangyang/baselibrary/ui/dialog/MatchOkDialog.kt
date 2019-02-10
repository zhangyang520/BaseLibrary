package com.zhangyang.baselibrary.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Display
import android.view.WindowManager
import android.widget.LinearLayout
import com.zhangyang.baselibrary.R

/**
 * Created by ASUS on 2017/9/22.
 * 匹配ok 对话框
 */
class MatchOkDialog : Dialog {

    lateinit var ll_dialog: LinearLayout
    lateinit var display: Display
    lateinit var textContent: String

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    constructor(context: Context, themeResId: Int, textContent: String) : super(context, themeResId) {
        this.textContent = textContent
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) :
                    super(context, cancelable, cancelListener) {}

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_match_ok)
        ll_dialog = findViewById<LinearLayout>(R.id.ll_dialog)
        setCancelable(false)

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay

        ll_dialog.layoutParams.width = (display.width * 0.8).toInt()
        ll_dialog.requestLayout()
    }

    /**
     * 弹框的消失
     */
    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
