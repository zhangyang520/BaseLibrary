package com.zhangyang.baselibrary.data.bean

/**
 *  刷新请求
 *  @Title ${name}
 *  @ProjectName ZCodeAssets
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/1520:39
 *
 */
enum class RefreshAction(var actionName:String) {
    PullDownRefresh("下拉刷新"),UpMore("上拉加载更多"),NormalAction("正常请求数据"),SearchAction("搜索数据")
}