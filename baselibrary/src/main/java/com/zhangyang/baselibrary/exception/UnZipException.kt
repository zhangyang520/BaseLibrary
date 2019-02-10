package com.zhangyang.baselibrary.exception

/**
 *
 *  @Author 张扬 @version 1.0
 *  @Date 2018/7/8 23:01
 */
class UnZipException : Exception {
    constructor() {}

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(cause: Throwable) : super(cause) {}
}