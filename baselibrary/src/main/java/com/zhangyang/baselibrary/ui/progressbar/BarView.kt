/*
 *    Copyright 2015 Kaopiz Software Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.suntray.chinapost.baselibrary.ui.progressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.jianhua.zcode.assets.baselibrary.UiUtils
import com.zhangyang.baselibrary.R


internal class BarView : View, Determinate {

    private var mOuterPaint: Paint? = null
    private var mInnerPaint: Paint? = null
    private var mBound: RectF? = null
    private var mInBound: RectF? = null
    private var mMax = 100
    private var mProgress = 0
    private var mBoundGap: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mOuterPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mOuterPaint!!.style = Paint.Style.STROKE
        mOuterPaint!!.strokeWidth = Helper.dpToPixel(2f, context).toFloat()
        mOuterPaint!!.color = Color.WHITE

        mInnerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInnerPaint!!.style = Paint.Style.FILL
        mInnerPaint!!.color = Color.WHITE

        mBoundGap = Helper.dpToPixel(5f, context).toFloat()
        mInBound = RectF(mBoundGap, mBoundGap,
                (width - mBoundGap) * mProgress / mMax, height - mBoundGap)

        mBound = RectF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = Helper.dpToPixel(2f, context)
        mBound!!.set(padding.toFloat(), padding.toFloat(), (w - padding).toFloat(), (h - padding).toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(mBound!!, mBound!!.height() / 2, mBound!!.height() / 2, mOuterPaint!!)
        canvas.drawRoundRect(mInBound!!, mInBound!!.height() / 2, mInBound!!.height() / 2, mInnerPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthDimension = UiUtils.instance.getDimen(R.dimen.barwidth)
        val heightDimension = UiUtils.instance.getDimen(R.dimen.barheight)
        setMeasuredDimension(widthDimension, heightDimension)

    }

    override fun setMax(max: Int) {
        this.mMax = max
    }

    override fun setProgress(progress: Int) {
        this.mProgress = progress
        mInBound!!.set(mBoundGap, mBoundGap,
                (width - mBoundGap) * mProgress / mMax, height - mBoundGap)
        invalidate()
    }
}
