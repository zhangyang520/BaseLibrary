package com.zhangyang.baselibrary.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 *   日期的工具类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/7/9 10:33
 */
object DateUtils {
    /**
     * 把毫秒转化成日期
     *
     * @param dateFormat(日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec(毫秒数)
     * @return
     */
    fun transferLongToDate(dateFormat: String, millSec: Long?): String {
        if (millSec == 0L || millSec == -1L) {
            return ""
        }
        //        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH); //英文格式
        val sdf = SimpleDateFormat(dateFormat, Locale.CANADA) //英文格式
        val date = Date(millSec!!)
        return sdf.format(date)
    }

    fun transferLongToDate(millSec: Long?): String {
        if (millSec!! < 1L) {
            return "00:00"
        } else {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val date = Date(millSec)
            return sdf.format(date)
        }
    }

    /**
     * 进行获取条目的类型
     *
     * @param time
     * @return
     */
    fun processAutoStateItemType(time: Long): String {
        //进行获取今天零时的时间
        val calendar = Calendar.getInstance()
        val todayTime = calendar.getTimeInMillis()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        //进行获取昨天的零时时间
        val yesterdayTime = calendar.getTimeInMillis() - 24 * 3600 * 1000
        //进行获取三天前零时时间
        val threedayTime = yesterdayTime - 24 * 3600 * 1000

        //进行比较
        if (todayTime - time <= 24 * 3600 * 1000 && todayTime - time > 0) { //确定是今天范围内
            //再次进行确定小时范围内
            val leftTime = todayTime - time
            if (leftTime / (3600 * 1000) > 0) {//小时单位
                return (leftTime / (3600 * 1000)).toString()+ "小时前"
            } else if (leftTime / (60 * 1000) > 0) {//分钟单位
                return (leftTime / (60 * 1000)).toString() + "分钟前"
            }
        } else if (time - yesterdayTime <= 24 * 3600 * 1000 && time - yesterdayTime > 0) {//确定昨天范围内
            calendar.setTimeInMillis(time)
            return formatMounthDays(calendar.get(Calendar.MONTH) + 1) + "/" +
                            formatMounthDays(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + calendar.get(Calendar.YEAR)
        } else if (time - threedayTime <= 24 * 3600 * 1000 && time - threedayTime > 0) {
            calendar.setTimeInMillis(time)
            return formatMounthDays(calendar.get(Calendar.MONTH) + 1) +
                        "/" + formatMounthDays(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + calendar.get(Calendar.YEAR)
        } else {
            calendar.setTimeInMillis(time)
            //其他类型
            return formatMounthDays(calendar.get(Calendar.MONTH) + 1) +
                    "/" + formatMounthDays(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + calendar.get(Calendar.YEAR)
        }
        return ""
    }


    private fun formatMounthDays(mounthOrDays: Int): String {
        var tempMounth = "" + mounthOrDays
        if (mounthOrDays < 10) {
            tempMounth = "0$mounthOrDays"
        }
        return tempMounth
    }

}