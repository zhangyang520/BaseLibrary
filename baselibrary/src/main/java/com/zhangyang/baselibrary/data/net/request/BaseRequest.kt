package com.zhangyang.baselibrary.data.net.request

/**
 * 基础上的实现类
 */
open class BaseRequest(private val machine_code:String,
                       private val token:String,
                       private  val user_id:String)