package com.zhangyang.baselibrary.injection.module

import com.trello.rxlifecycle.LifecycleProvider
import dagger.Module
import dagger.Provides

/**
 *  生命周期管理的LifecylerProvider 的module
 *  @Author 张扬 @version 1.0
 *  @Date 2018/7/1 21:47
 */
@Module
class LifecylerProviderModule constructor(var lifecycleProvider: LifecycleProvider<*>){

    @Provides
    fun providerLifecylerProvider():LifecycleProvider<*>{
        return lifecycleProvider;
    }
}