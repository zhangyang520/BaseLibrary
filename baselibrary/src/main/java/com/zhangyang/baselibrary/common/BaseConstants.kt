package com.zhangyang.baselibrary.common

/**
 * Created by zhangyang on 2018/5/17 16:46.
 * 基础常量类
 * version 1
 */
object BaseConstants {

        /**
         * 请求失败展示信息
         */
        val ERROR_MESSAGE = "请求失败，请稍后再试"
        var SUCCESS = "200";//成功的状态

        //http://localhost:8080/admin/mobile/   http://localhost:8090/
        var BASE_URL = "http://47.94.207.62:19999/admin/mobile/"//生产环境1010
        var BASE_UPLOAD_URL="http://47.94.207.62:19999/admin/"
        var BASE_DOWNLOAD_URL="http://47.94.207.62:19999/admin/"
        var DEFALUT_PRINT_VALUE = "b"//缺省的当前是否打印的变量
        var CURRENT_PRINT_VALUE = "a"//当前的打印的值
        val versionCode = 10

        //登录相关的url
        const val LOGIN = "login.do"


        //目录的常量
        val ROOTPATH = "chinaPost"
        val picCache = "picCache"
        val zipDir = "zipDir"
        val uploadPic = "uploadPic"
        val downloadDir = "downloadDir"
        val recordPathDir = "recordPathDir"
        val debugPath = "debugPath"
        val newTaskPath = "newTaskPath"
        //进行控制是否将文件保存在sd卡中
        val IS_SD_CAN = true

        /**
         * 需要解析类型的判断
         */
        //权限的请求吗
        val CAMERA = 101
        val WRITE_EXTERNAL_STORAGE = 102
        val REDA_EXTERNAL_STORAGE = 103

        /***
         * 模块的分类型
         */
        val STRING_GAP = "--------"//字符串的分割符
        //最大的进度的值
        val MAX_PROGRESS = 1000
        //问题
        val QUESTION_STRING = "Question "

        val XIAOMI_TIME_WUCHA = 500//小米手机的时间的误差


        val YUNXIN_VOLUME_START_VALUE = 100//云信的相关的初始化值
        val YUNXIN_VOLUME_CHAZHI = 600//云信的相关的差值

        //友盟统计的点击事件的ID
        val CLASS_FOUR_JI = "0"
        val KEY_SP_TOKEN: String = "KEY_SP_TOKEN"
        val TABLE_PREFS: String = "TABLE_PREFS"
        val NO_LOGIN_TYPE: Int=101;

        var DB_VERSION=11 //增加了 orgId

        val SUCCESS_RESULT_PROGRESS="成功结果"
}