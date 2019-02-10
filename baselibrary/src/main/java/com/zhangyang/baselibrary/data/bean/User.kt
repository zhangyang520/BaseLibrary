package com.zhangyang.baselibrary.data.bean

import com.lidroid.xutils.db.annotation.Id
import com.lidroid.xutils.db.annotation.Table

/**
 * Created by zhangyang on 2017/2/16 19:46.
 * 用户实体的封装  !
 * version 1
 */
@Table
class User {
    var id: Int = 0
    var token: String ?=null //token码
    @Id
    var userId: Int=0 //用户的id
    var isLocalUser: Boolean = false//是否是登录用户
    var headImgPath:String="" //上传头像的路径
    var phone:String="" //电话号码
    var email:String=""//用户名
    var orgName:String=""//集团总部
    var nickName:String=""//昵称
    var emailAddres:String=""//邮箱地址
    var pwd:String=""
    var userRole:Int=-1
    var orgId:String=""

    override fun toString(): String {
        return "User(id=$id, token=$token, userId=$userId, isLocalUser=$isLocalUser, headImgPath='$headImgPath', phone='$phone', email='$email', orgName='$orgName', nickName='$nickName', emailAddres='$emailAddres', pwd='$pwd', userRole=$userRole, orgId='$orgId')"
    }
}
