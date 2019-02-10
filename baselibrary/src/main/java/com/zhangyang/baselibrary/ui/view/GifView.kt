package com.suntray.chinapost.baselibrary.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Movie
import android.os.Build
import android.util.AttributeSet
import android.view.View

import java.io.FileInputStream
import java.io.FileNotFoundException
import com.jianhua.zcode.assets.baselibrary.UiUtils
import com.zhangyang.baselibrary.R

/**
 * Created by suntray.chinapost on // :
 * version
 */

class GifView:View{
    private var mMovieResourceId: Int = 0
    private var mMovie: Movie? = null

    private var mMovieStart: Long = 0

    private var mCurrentAnimationTime = 0

    private var mLeft: Float = 0.toFloat()

    private var mTop: Float = 0.toFloat()

    private var mScale: Float = 0.toFloat()

    private var mMeasuredMovieWidth: Int = 0

    private var mMeasuredMovieHeight: Int = 0

    private var mVisible = true

    @Volatile private var mPaused = false

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, R.styleable.CustomTheme_gifViewStyle)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        setViewAttributes(context!!, attrs!!, defStyleAttr);
    }

    @SuppressLint("NewApi")
    private fun setViewAttributes(context: Context, attrs: AttributeSet,
                                  defStyle: Int) {
        // 从描述文件中读出gif的值，创建出Movie实例
        val array = context.obtainStyledAttributes(attrs,
                R.styleable.GifView, defStyle, -1)
        mMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1)
        mPaused = array.getBoolean(R.styleable.GifView_paused, false)
        array.recycle()
        if (mMovieResourceId != -1) {
            mMovie = Movie.decodeStream(resources.openRawResource(
                    mMovieResourceId))
        }
    }

    /**
     * 设置gif图资源

     * @param movieResId
     */
    fun setMovieResource(movieResId: Int) {
        this.mMovieResourceId = movieResId
        mMovie = Movie.decodeStream(resources.openRawResource(
                mMovieResourceId))
        requestLayout()
    }

    @Throws(FileNotFoundException::class)
    fun setMovieFilePath(movieResId: String) {
        mMovie = Movie.decodeStream(FileInputStream(movieResId))
        UiUtils.instance.getHandler().post(Runnable { requestLayout() })
    }

    var movie: Movie
        get() = mMovie!!
        set(movie) {
            this.mMovie = movie
            requestLayout()
        }

    fun setMovieTime(time: Int) {
        mCurrentAnimationTime = time
        invalidate()
    }

    /**
     * 判断gif图是否停止了

     * @return
     */
    /**
     * 设置暂停

     * @param paused
     */
    var isPaused: Boolean
        get() = this.mPaused
        set(paused) {
            this.mPaused = paused
            if (!paused) {
                mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime
            }
            UiUtils.instance.getHandler().post(Runnable { invalidate() })
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mMovie != null) {
            val movieWidth = mMovie!!.width()
            val movieHeight = mMovie!!.height()
            val maximumWidth = View.MeasureSpec.getSize(widthMeasureSpec)
            val scaleW = movieWidth.toFloat() / maximumWidth.toFloat()
            mScale = 1f / scaleW
            mMeasuredMovieWidth = maximumWidth
            mMeasuredMovieHeight = (movieHeight * mScale).toInt()
            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight)
        } else {
            setMeasuredDimension(suggestedMinimumWidth,
                    suggestedMinimumHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mLeft = (width - mMeasuredMovieWidth) / 1f
        mTop = (height - mMeasuredMovieHeight) / 1f
        mVisible = visibility == View.VISIBLE
    }

    override fun onDraw(canvas: Canvas) {
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime()
                drawMovieFrame(canvas)
                invalidateView()
            } else {
                drawMovieFrame(canvas)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun invalidateView() {
        if (mVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation()
            } else {
                invalidate()
            }
        }
    }

    private fun updateAnimationTime() {
        val now = android.os.SystemClock.uptimeMillis()
        // 如果第一帧，记录起始时间
        if (mMovieStart == 0L) {
            mMovieStart = now
        }
        // 取出动画的时长
        var dur = mMovie!!.duration()
        if (dur == 0) {
            dur = DEFAULT_MOVIE_DURATION
        }
        // 算出需要显示第几帧
        mCurrentAnimationTime = ((now - mMovieStart) % dur).toInt()
    }

    private fun drawMovieFrame(canvas: Canvas) {
        // 设置要显示的帧，绘制即可
        mMovie!!.setTime(mCurrentAnimationTime)
//        canvas.save(Canvas.M)
        canvas.scale(mScale, mScale)
        mMovie!!.draw(canvas, mLeft / mScale, mTop / mScale)
        canvas.restore()
    }

    @SuppressLint("NewApi")
    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        mVisible = screenState == View.SCREEN_STATE_ON
        invalidateView()
    }

    @SuppressLint("NewApi")
    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        mVisible = visibility == View.VISIBLE
        invalidateView()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        mVisible = visibility == View.VISIBLE
        invalidateView()
    }

    companion object {
        /**
         * 默认为秒

         */
        private val DEFAULT_MOVIE_DURATION = 1000
    }

}
