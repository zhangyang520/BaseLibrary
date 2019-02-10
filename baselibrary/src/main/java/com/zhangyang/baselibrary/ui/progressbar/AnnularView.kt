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
import com.zhangyang.baselibrary.R


internal class AnnularView : View, Determinate {

    private var mWhitePaint: Paint? = null
    private var mGreyPaint: Paint? = null
    private var mBound: RectF? = null
    private var mMax = 100
    private var mProgress = 0


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        mWhitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mWhitePaint!!.style = Paint.Style.STROKE
        mWhitePaint!!.strokeWidth = Helper.dpToPixel(3f, getContext()).toFloat()
        mWhitePaint!!.color = Color.WHITE

        mGreyPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mGreyPaint!!.style = Paint.Style.STROKE
        mGreyPaint!!.strokeWidth = Helper.dpToPixel(3f, getContext()).toFloat()
        mGreyPaint!!.color = context.resources.getColor(R.color.kprogresshud_grey_color)


        mBound = RectF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = Helper.dpToPixel(4f, context)
        mBound!!.set(padding.toFloat(), padding.toFloat(), (w - padding).toFloat(), (h - padding).toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val mAngle = mProgress * 360f / mMax
        canvas.drawArc(mBound!!, 270f, mAngle, false, mWhitePaint!!)
        canvas.drawArc(mBound!!, 270 + mAngle, 360 - mAngle, false, mGreyPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val dimension = Helper.dpToPixel(40f, context)
        setMeasuredDimension(dimension, dimension)
    }

    override fun setMax(max: Int) {
        this.mMax = max
    }

    override fun setProgress(progress: Int) {
        mProgress = progress
        invalidate()
    }
}
