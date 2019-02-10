package com.zhangyang.baselibrary.data.dao

import com.lidroid.xutils.db.annotation.Id

/**
 * 数据库的基础类
 */
open class BaseDBEntity {

    @Id
    lateinit var id:Integer
}