package com.zhangyang.baselibrary.data.dao

import com.zhangyang.baselibrary.common.BaseApplication
import com.zhangyang.baselibrary.common.BaseConstants
import com.zhangyang.baselibrary.data.bean.User
import com.zhangyang.baselibrary.exception.ContentException
import com.lidroid.xutils.db.sqlite.Selector
import com.lidroid.xutils.exception.DbException

/**
 * 用户相关的dao
 * @Author 张扬 @version 1.0
 * @Date 2018/6/15 15:42
 */
object UserDao {

    /**
     * 查找数据
     * @param
     * @return
     */
    @Throws(ContentException::class)
    fun getall(): List<User>
    {
            try {
                val datas = BaseApplication.dbUtils.findAll(User::class.java)
                if (datas == null || datas.size == 0) {
                    throw ContentException("没有用户列表!")
                }
                return datas
            } catch (e: DbException) {
                e.printStackTrace()
                throw RuntimeException()
            }

     }

    /**
     * 进行获取本地用户
     * @return
     */
    @Throws(ContentException::class)
    fun getLocalUser(): User
    {
            try {
                val datas = BaseApplication.dbUtils.findAll<User>(Selector.from(User::class.java).where("isLocalUser", "=", true))
                return if (datas != null && datas.size > 0) {
                    datas[0]
                } else {
                    throw ContentException(BaseConstants.NO_LOGIN_TYPE)
                }
            } catch (e: DbException) {
                e.printStackTrace()
                throw RuntimeException()
            }

    }

    /**
     * 删除 对象
     */
    fun deleteUser(user:User) {
        BaseApplication.dbUtils.delete(user)
    }
    /**
     * 保存所有数据
     * @param user
     */
    fun saveAll(user: User) {
        try {
            BaseApplication.dbUtils.save(user)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException("保存数据失败!")
        }

    }


    /**
     * 保存更新数据
     * @param user
     */
    fun saveUpDate(user: User) {
        try {
            BaseApplication.dbUtils.saveOrUpdate(user)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException("保存数据失败!")
        }

    }

    /**
     * 得到User对象的UserId
     * @param userId
     * @return
     */
    @Throws(ContentException::class)
    fun getUserId(userId: String): User {
        try {
            val datas = BaseApplication.dbUtils.findAll<User>(Selector.from(User::class.java).where("id", "=", userId))
            return if (datas!=null && datas.size > 0) {
                datas[0]
            } else {
                throw ContentException(BaseConstants.NO_LOGIN_TYPE)
            }
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException()
        }

    }

    /**
     * 进行更新所有用户的本地身份状态
     * @param flag
     */
    fun updateAllUserLocalState(flag: Boolean) {
        try {
            val userList = getall()
            for (user in userList) {
                user.isLocalUser = flag
            }
            BaseApplication.dbUtils.updateAll(userList, "isLocalUser")
        } catch (e: ContentException) {
            //没有用户
            e.printStackTrace()
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException()
        }

    }

    /**
     * 进行更新除了该用户所有用户的本地身份状态
     * @param flag
     */
    fun updateExceptUserLocalState(userId: String, flag: Boolean) {
        try {
            val userList = getall()
            for (user in userList) {
                if (!user.id.toString().equals(userId)) {
                    //设置其他用户为false
                    user.isLocalUser = false
                } else {
                    //对应的userId用户为flag
                    user.isLocalUser = flag
                }
            }
            BaseApplication.dbUtils.updateAll(userList)
        } catch (e: ContentException) {
            //没有用户
            e.printStackTrace()
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException()
        }
    }
}
