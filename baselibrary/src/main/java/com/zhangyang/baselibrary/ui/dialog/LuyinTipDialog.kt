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
 *   单点登录的提示框
 *  @Author 张扬 @version 1.0
 *  @Date 2018/7/2 17:10
 */
class LuyinTipDialog :Dialog, View.OnClickListener {
    var layoutRes: Int = 0//布局文件
    var tips = "录音完毕,直接进入结果界面"
    lateinit var tv_tips: TextView
    lateinit var tv_ok: Button
    private var listener: CustomDialogListener? = null
    private var display: Display? = null
    private var ll_dialog: LinearLayout? = null

    override fun onClick(v: View) {
        if (listener != null) {
            listener!!.onClick(v)
        }
    }

    interface CustomDialogListener {
        fun onClick(view: View)
    }


    constructor(context: Context):super(context, R.style.simulationDialog){
    }

    constructor(context: Context, tips: String): super(context, R.style.simulationDialog) {
        this.tips = tips
    }

    constructor(context: Context, theme: Int, tips: String):super(context, theme){
        this.tips = tips
    }

    /**
     * 自定义布局的构造方法
     *
     * @param context
     * @param resLayout
     */
    constructor(context: Context, resLayout: Int, listener: CustomDialogListener): super(context) {
        this.layoutRes = resLayout
        this.listener = listener
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     * @param resLayout
     */
     constructor(context: Context, theme: Int, resLayout: Int, listener: CustomDialogListener): super(context, theme) {
        this.layoutRes = resLayout
        this.listener = listener
    }

    override  protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.luyin_tipdialog)
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
        tv_ok = findViewById<Button>(R.id.tv_ok)
        tv_ok.setOnClickListener(this)
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

    fun getListener(): CustomDialogListener? {
        return listener
    }

    fun setListener(listener: CustomDialogListener) {
        this.listener = listener
    }

    /**
     * 弹框的消失
     */
    override  fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override  fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}