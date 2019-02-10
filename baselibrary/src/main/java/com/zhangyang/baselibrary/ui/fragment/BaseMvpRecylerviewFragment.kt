package com.zhangyang.baselibrary.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dinuscxj.refresh.RecyclerRefreshLayout
import com.zhangyang.baselibrary.R
import com.zhangyang.baselibrary.common.BaseApplication
import com.zhangyang.baselibrary.injection.component.ActivityComponent
import com.zhangyang.baselibrary.injection.component.DaggerActivityComponent
import com.zhangyang.baselibrary.injection.module.ActivityModule
import com.zhangyang.baselibrary.injection.module.LifecylerProviderModule
import com.zhangyang.baselibrary.presenter.BasePresenter
import com.zhangyang.baselibrary.presenter.view.BaseView
import com.zhangyang.baselibrary.ui.progressbar.KProgressHUD
import com.zhangyang.baselibrary.ui.recylerviewRefrsehLayout.adapter.HeaderViewRecyclerAdapter
import com.zhangyang.baselibrary.ui.recylerviewRefrsehLayout.adapter.RecyclerListAdapter
import com.zhangyang.baselibrary.ui.recylerviewRefrsehLayout.model.CursorModel
import com.zhangyang.baselibrary.ui.recylerviewRefrsehLayout.tips.DefaultTipsHelper
import com.zhangyang.baselibrary.ui.recylerviewRefrsehLayout.tips.TipsHelper
import com.zhangyang.baselibrary.utils.ToastUtil

import javax.inject.Inject


/**
 *  基础的fragment对应的recylerview 列表的 fragment
 *  Created by ASUS on 2018/5/25.
 *  基础的mvp框架的activity类
 */
 abstract class BaseMvpRecylerviewFragment<T: BasePresenter<*>,MODEL : CursorModel>: BaseFragment<T>(), BaseView {

     val SIMULATE_UNSPECIFIED = 0
     val SIMULATE_FRESH_FIRST = 1
     val SIMULATE_FRESH_NO_DATA = 2
     val SIMULATE_FRESH_FAILURE = 3
     val REQUEST_DURATION = 800
     var mSimulateStatus: Int = 0

    var pageNumber=1;
    var pagePreNumber=-1
    val pageCount=10

    protected lateinit  var activityComponent: ActivityComponent;
    var hud2: KProgressHUD?=null

    private var mIsLoading: Boolean = false

    var mRecyclerView: RecyclerView? = null
    private var mRecyclerRefreshLayout: RecyclerRefreshLayout? = null

    private var mTipsHelper: TipsHelper? = null
    private var mHeaderAdapter: HeaderViewRecyclerAdapter? = null
    private var mOriginAdapter: RecyclerListAdapter<MODEL, *>? = null

     var mInteractionListener: InteractionListener? = null

    private val mRefreshEventDetector = RefreshEventDetector()
    private val mAutoLoadEventDetector = AutoLoadEventDetector()

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAcitivityInjection();
        injectCompontent()
    }

    private fun initAcitivityInjection() {
        activityComponent= DaggerActivityComponent.builder().
                                            activityModule(ActivityModule(activity)).
                                            lifecylerProviderModule(LifecylerProviderModule(this)).
                                            appComponent((activity.applicationContext as BaseApplication).appComponent).build();
    }

    /**
     * 进行获取 基本的view
     */
    override fun getContentView(): View {
        return View.inflate(context, R.layout.base_refresh_recycler_list_layout,null)
    }

    /**
     * 当 view 创建 好的时候 进行处理业务
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(root!!)
        initRecyclerRefreshLayout(root!!)
        initView();
        mInteractionListener = createInteraction()
        mTipsHelper = createTipsHelper()
        processBussiness()
    }

    /**
     * 注入component
     */
    abstract fun injectCompontent()

    /**
     * 基础的basePresenter的类
     */
    @Inject
    lateinit var  basePresenter:T


    /**
     * 展示进度条
     */
    override fun showLoading(){
        if(hud2!=null && !hud2!!.isShowing){
            hud2?.show()
        }
   }

    /**
     * 隐藏进度条
     */
    override fun hideLoading(){
        if(hud2!=null && hud2!!.isShowing){
            hud2?.dismiss()
        }
    }

    /**
     * 错误的反馈
     */
    override fun onError(error:String,vararg args:String){
        ToastUtil.makeText(activity,error);
    }

    /**
     * 登录失败的反馈
     */
    override fun onLoginError(error: String, code: String) {
        outLoginState(activity,error);
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun initRecyclerView(view: View) {
        mRecyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView

        mRecyclerView!!.addOnScrollListener(mAutoLoadEventDetector)

        val layoutManager = onCreateLayoutManager()
        if (layoutManager != null) {
            mRecyclerView!!.setLayoutManager(layoutManager)
        }

        mOriginAdapter = createAdapter()
        mHeaderAdapter = HeaderViewRecyclerAdapter(mOriginAdapter)
        mRecyclerView!!.setAdapter(mHeaderAdapter)
        mHeaderAdapter!!.adjustSpanSize(mRecyclerView)
    }

    private fun initRecyclerRefreshLayout(view: View) {
        mRecyclerRefreshLayout = view.findViewById<View>(R.id.refresh_layout) as RecyclerRefreshLayout

        if (mRecyclerRefreshLayout == null) {
            return
        }

        if (allowPullToRefresh()) {
            mRecyclerRefreshLayout!!.setNestedScrollingEnabled(true)
            mRecyclerRefreshLayout!!.setOnRefreshListener(mRefreshEventDetector)
        } else {
            mRecyclerRefreshLayout!!.setEnabled(false)
        }
    }

    abstract fun createAdapter(): RecyclerListAdapter<MODEL, *>

    protected fun onCreateLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity)
    }

    protected fun createTipsHelper(): TipsHelper {
        return DefaultTipsHelper(this)
    }

    open fun createInteraction(): InteractionListener? {
        return null
    }

    override fun onDestroyView() {
        mRecyclerView!!.removeOnScrollListener(mAutoLoadEventDetector)
        super.onDestroyView()
    }

    fun getHeaderAdapter(): HeaderViewRecyclerAdapter {
        return mHeaderAdapter!!
    }

    fun getOriginAdapter(): RecyclerListAdapter<MODEL, *> {
        return mOriginAdapter!!
    }

    fun getRecyclerRefreshLayout(): RecyclerRefreshLayout {
        return mRecyclerRefreshLayout!!
    }

    fun getRecyclerView(): RecyclerView {
        return mRecyclerView!!
    }

    fun allowPullToRefresh(): Boolean {
        return true
    }

    fun refresh() {
        if (isFirstPage()) {
            mTipsHelper!!.showLoading(true)
        } else {
            mRecyclerRefreshLayout!!.setRefreshing(true)
        }
        requestRefresh()
    }

    fun isFirstPage(): Boolean {
        return mOriginAdapter!!.getItemCount() <= 0
    }

    inner class RefreshEventDetector : RecyclerRefreshLayout.OnRefreshListener {

        override fun onRefresh() {
            requestRefresh()
        }
    }

    inner class AutoLoadEventDetector : RecyclerView.OnScrollListener() {

        override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
            val manager = view!!.layoutManager
            if (manager.childCount > 0) {
                val count = manager.itemCount
                val last = (manager
                        .getChildAt(manager.childCount - 1).layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

                println("AutoLoadEventDetector onScrolled count:"+count+"..last:"+last+"...mIsLoading:"+mIsLoading+"..mInteractionListener is null:"+(mInteractionListener==null)+"...dy:"+dy)
                if (last == count - 1 && !mIsLoading && mInteractionListener != null && dy>0) {
                    //同时 需要向下 滑动！
                    requestMore()
                }
            }
        }
    }

    private fun requestRefresh() {
        if (mInteractionListener != null && !mIsLoading) {
            mIsLoading = true
            mInteractionListener!!.requestRefresh()
        }
    }

    private fun requestMore() {
        if (mInteractionListener != null && mInteractionListener!!.hasMore() && !mIsLoading) {
            mIsLoading = true
            mInteractionListener!!.requestMore(null)
        }
    }

    /**
     * 交互的监听器
     */
    abstract inner class InteractionListener {
        open fun requestRefresh() {
            requestComplete()
            if (mOriginAdapter!!.isEmpty()) {
                mTipsHelper!!.showEmpty()
            } else if (hasMore()) {
                mTipsHelper!!.showHasMore()
            } else {
                mTipsHelper!!.hideHasMore()
            }
        }

        open  fun requestMore(openProjectModels: List<Any>?) {
            requestComplete()
            if(openProjectModels!!.size==0){
                mTipsHelper!!.hideHasMore()
            }
        }

        //请求搜索
        open fun requestSearch(){
           requestComplete()
            if (mOriginAdapter!!.isEmpty()) {
                mTipsHelper!!.showEmpty()
            } else if (hasMore()) {
                mTipsHelper!!.showHasMore()
            } else {
                mTipsHelper!!.hideHasMore()
            }
        }

        //刚进入的请求
        open fun requestNormal(){
            requestComplete()
            if (mOriginAdapter!!.isEmpty()) {
                mTipsHelper!!.showEmpty()
            } else if (hasMore()) {
                mTipsHelper!!.showHasMore()
            } else {
                mTipsHelper!!.hideHasMore()
            }
        }
        fun requestFailure(errorContent:String) {
            requestComplete()
            mTipsHelper!!.showError(isFirstPage(), Exception(errorContent))
        }

        open fun requestComplete() {
            mIsLoading = false
            if (mRecyclerRefreshLayout != null) {
                mRecyclerRefreshLayout!!.setRefreshing(false)
            }
            mTipsHelper!!.hideError()
            mTipsHelper!!.hideEmpty()
            mTipsHelper!!.hideLoading()
        }

        fun hasMore(): Boolean {
            if(mOriginAdapter!!.getItemList()!=null && mOriginAdapter!!.getItemList().size>=3){
                return true
            }else{
                return false
            }
//            return mOriginAdapter!!.getItem(mOriginAdapter!!.getItemCount() - 1).hasMore()
        }
    }
}