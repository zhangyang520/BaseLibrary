package com.zhangyang.baselibrary.ui.pager

import com.trello.rxlifecycle.LifecycleProvider
import com.trello.rxlifecycle.LifecycleTransformer
import com.trello.rxlifecycle.RxLifecycle
import com.trello.rxlifecycle.android.ActivityEvent
import com.trello.rxlifecycle.android.RxLifecycleAndroid
import rx.Observable
import rx.subjects.BehaviorSubject

/**
 *  @Author 张扬 @version 1.0
 *  @Date 2018/7/10 16:25
 */
open class PagerLifecyclerProvider:LifecycleProvider<ActivityEvent>{
    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    override fun lifecycle(): Observable<ActivityEvent> {
        return this.lifecycleSubject.asObservable()
    }

    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event)
    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(this.lifecycleSubject)
    }
}