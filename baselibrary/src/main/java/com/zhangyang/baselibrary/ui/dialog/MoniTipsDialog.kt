package com.zhangyang.baselibrary.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Display
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.zhangyang.baselibrary.R


/**
 * Created by zhangyang on 2017/3/8.
 * 全真模拟对应的提示框:
 */

class MoniTipsDialog : Dialog, View.OnClickListener {

    internal var layoutRes: Int = 0//布局文件
    internal var context: Context
    internal var tips = "小组互动实战已中断"
    lateinit var tv_tips: TextView
    lateinit var tv_ok: Button
    lateinit var tv_repipei: Button
    var listener: CustomDialogListener? = null
    private var display: Display? = null
    private var ll_dialog: LinearLayout? = null
    var str_ok = "返回"
    var str_repipei = "重新匹配"
    var isSingleBtn = false//是否显示单个按钮

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_fanhui -> if (listener != null) {
                listener!!.onFanhui(v)
            }

            R.id.tv_repipei -> if (listener != null) {
                listener!!.onRepipei(v)
            }
        }

    }

    interface CustomDialogListener {
        fun onFanhui(view: View)
        fun onRepipei(view: View)
    }


    constructor(context: Context) : super(context, R.style.simulationDialog) {
        this.context = context
    }


    constructor(context: Context, theme: Int, tips: String) : super(context, theme) {
        this.context = context
        this.tips = tips
    }

    constructor(context: Context, theme: Int, tips: String, singleBtn: Boolean) : super(context, theme) {
        this.context = context
        this.tips = tips
        this.isSingleBtn = singleBtn
    }

    /**
     * 自定义布局的构造方法
     * @param context
     * @param resLayout
     */
    constructor(context: Context, resLayout: Int, listener: CustomDialogListener, singleBtn: Boolean) : super(context) {
        this.context = context
        this.layoutRes = resLayout
        this.listener = listener
        this.isSingleBtn = singleBtn
    }

    /**
     * 自定义布局的构造方法
     *
     * @param context
     */
    constructor(context: Context, listener: CustomDialogListener, content: String, str_ok: String, str_repipei: String, singleBtn: Boolean) : super(context) {
        this.context = context
        this.tips = content
        this.str_ok = str_ok
        this.str_repipei = str_repipei
        this.listener = listener
        this.isSingleBtn = singleBtn
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     * @param resLayout
     */
    constructor(context: Context, theme: Int, resLayout: Int, listener: CustomDialogListener) : super(context, theme) {
        this.context = context
        this.layoutRes = resLayout
        this.listener = listener
    }


    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moni_tipdialog)
        val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
        initView()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else super.onKeyDown(keyCode, event)
    }

    /**
     * TextView tv_tips;
     * Button tv_ok;
     */
    private fun initView() {
        ll_dialog = findViewById<LinearLayout>(R.id.ll_dialog)
        tv_tips = findViewById<TextView>(R.id.tv_tips)
        tv_tips.text = tips
        tv_ok = findViewById<Button>(R.id.tv_fanhui)
        tv_ok.text = this.str_ok
        tv_ok.setOnClickListener(this)
        tv_repipei = findViewById<Button>(R.id.tv_repipei)
        if (isSingleBtn) {
            tv_repipei.visibility = View.GONE
        } else {
            tv_repipei.visibility = View.VISIBLE
            tv_repipei.text = this.str_repipei
            tv_repipei.setOnClickListener(this)
        }
        setCancelable(false)
        ll_dialog!!.layoutParams = FrameLayout.LayoutParams((display!!.width * 0.8).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    /**
     * 进行设置提示框
     *
     * @param contentText
     */
    fun setContentText(contentText: String) {

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