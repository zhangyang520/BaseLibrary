package com.zhangyang.baselibrary.injection.module

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by zhangyang on 2018/5/18 19:36.
 * version 1
 */
@Module
class AppModule(private val context:Context){


    @Provides
    fun provideContext():Context{
        return context
    }
}