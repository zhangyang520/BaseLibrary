package com.zhangyang.baselibrary.data.bean

/**
 * Created by ASUS on 2018/5/29.
 * 基础的信息类:
 *  restful 模式的设置
 *  status  的响应码
 *  message 消息内容
 *  data  响应的数据
 */
data class BaseResp<out T>(val status:String,val message:String,val data:T)