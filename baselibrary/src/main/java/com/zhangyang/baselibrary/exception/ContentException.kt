package com.zhangyang.baselibrary.exception

/**
 * Created by zhangyang on 2018/5/17 17:15.
 * version 1
 */
class ContentException : Exception {

        // 错误的代码的叙述
        var errorCode: Int = 0
        var errorContent = ""//异常描述
        var code: String? = null
        var obj: Any?=null


        constructor() : super() {}

        constructor(errorCode: Int) : super() {
            this.errorCode = errorCode
        }

        constructor(errorContent: String) : super(errorContent) {
            this.errorContent = errorContent
        }


        /**
         * 构造函数
         * @param Code
         * *
         * @param obj
         */
        constructor(Code: String, `obj`: Any, errorContent: String) : super(Code) {
            this.code = Code
            this.`obj` = `obj`
            this.errorContent = errorContent
        }

        /**
         * 构造函数
         * @param Code
         * *
         * @param obj
         */
        constructor(Code: String, `obj`: Any) : super(Code) {
            this.code = Code
            this.`obj` = `obj`
            this.errorContent = errorContent
        }

        constructor(errorCode: Int, errorContent: String) {
            this.errorCode = errorCode
            this.errorContent = errorContent
        }

        constructor(errorContent: String, Code: String) {
            this.code = Code
            this.errorContent = errorContent
        }


        constructor(detailMessage: String, throwable: Throwable) : super(detailMessage, throwable) {}

        constructor(throwable: Throwable) : super(throwable) {}
}
