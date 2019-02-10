package com.zhangyang.baselibrary.utils

//import android.util.//Log;

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.telephony.TelephonyManager
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.ListView
import com.zhangyang.baselibrary.R
import com.zhangyang.baselibrary.common.BaseConstants
import com.zhangyang.baselibrary.common.CrashHandler
import com.zhangyang.baselibrary.exception.ContentException
import com.zhangyang.baselibrary.ui.dialog.MoniTipsDialog
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.ByteBuffer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * <通用工具类>
 *
 * @author wangzhenqi
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 *
 * @since [V1]
</通用工具类> */
object GeneralUtils {

    /**
     * <获取当前日期 格式 yyyyMMddHHmmss> <功能详细描述>
     *
     * @return String
     * @see [类、类.方法、类.成员]
    </功能详细描述></获取当前日期> */
    val rightNowDateString: String
        get() {
            val calendar = Calendar.getInstance(Locale.CHINA)
            val date = calendar.time
            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
            return dateFormat.format(date)
        }

    /**
     * <获取当前时间 格式yyyyMMddHHmmss> <功能详细描述>
     *
     * @return String
     * @see [类、类.方法、类.成员]
    </功能详细描述></获取当前时间> */
    val rightNowDateTime: Date?
        get() {
            val calendar = Calendar.getInstance(Locale.CHINA)
            val date = calendar.time
            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
            try {
                return dateFormat.parse(dateFormat.format(date))
            } catch (e: ParseException) {
                e.printStackTrace()
                return null
            }

        }

    /**
     * 获取设备型号
     *
     * @return
     */
    val deviceModel: String
        get() = android.os.Build.MODEL

    /**
     * 展示dialog
     * @param context
     * @param listener
     * @return
     */
    var moniTipsDialog: MoniTipsDialog?= null

    /**
     * 进行展示对应的内容
     * @param context
     * @param listener
     * @param tipsContent
     * @return
     */
     var moniTipsDialog2: MoniTipsDialog?=null

    //版本号
    fun getVersionCode(context: Context): Int {
        return getPackageInfo(context)!!.versionCode
    }

    private fun getPackageInfo(context: Context): PackageInfo? {
        var pi: PackageInfo? = null
        try {
            val pm = context.packageManager
            pi = pm.getPackageInfo(context.packageName,
                    PackageManager.GET_CONFIGURATIONS)
            return pi
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pi
    }

    /**
     * 判断对象是否为null , 为null返回true,否则返回false
     *
     * @param obj 被判断的对象
     * @return boolean
     */
    fun isNull(obj: Any?): Boolean {
        return if (null == obj) true else false
    }

    /**
     * 判断对象是否为null , 为null返回false,否则返回true
     *
     * @param obj 被判断的对象
     * @return boolean
     */
    fun isNotNull(obj: Any): Boolean {
        return !isNull(obj)
    }

    /**
     * 判断字符串是否为null或者0长度，字符串在判断长度时，先去除前后的空格,空或者0长度返回true,否则返回false
     *
     * @param str 被判断的字符串
     * @return boolean
     */
    fun isNullOrZeroLenght(str: String?): Boolean {
        return if (null == str || "" == str.trim { it <= ' ' }) true else false
    }

    /**
     * 判断字符串是否为null或者0长度，字符串在判断长度时，先去除前后的空格,空或者0长度返回false,否则返回true
     *
     * @param str 被判断的字符串
     * @return boolean
     */
    fun isNotNullOrZeroLenght(str: String): Boolean {
        return !isNullOrZeroLenght(str)
    }

    /**
     * 判断集合对象是否为null或者0大小 , 为空或0大小返回true ,否则返回false
     *
     * @param c collection 集合接口
     * @return boolean 布尔值
     * @see [类、类.方法、类.成员]
     */
    fun isNullOrZeroSize(c: Collection<Any>): Boolean {
        return isNull(c) || c.isEmpty()
    }

    /**
     * 判断集合对象是否为null或者0大小 , 为空或0大小返回false, 否则返回true
     *
     * @param c collection 集合接口
     * @return boolean 布尔值
     * @see [类、类.方法、类.成员]
     */
    fun isNotNullOrZeroSize(c: Collection<Any>): Boolean {
        return !isNullOrZeroSize(c)
    }

    /**
     * 判断数字类型是否为null或者0，如果是返回true，否则返回false
     *
     * @param number 被判断的数字
     * @return boolean
     */
    fun isNullOrZero(number: Number): Boolean {
        return if (GeneralUtils.isNotNull(number)) {
            if (number.toInt() != 0) false else true
        } else true
    }

    /**
     * 判断数字类型是否不为null或者0，如果是返回true，否则返回false
     *
     * @param number 被判断的数字
     * @return boolean
     */
    fun isNotNullOrZero(number: Number): Boolean {
        return !isNullOrZero(number)
    }

    /**
     * <保留两位有效数字> <功能详细描述>
     *
     * @param num String
     * @return String
     * @see [类、类.方法、类.成员]
    </功能详细描述></保留两位有效数字> */
    fun retained2SignificantFigures(num: String): String {
        return BigDecimal(num).setScale(2, RoundingMode.HALF_UP).toString()
    }

    /**
     * <减法运算并保留两位有效数字> <功能详细描述>
     *
     * @param subt1 String
     * @param subt2 String
     * @return String
     * @see [类、类.方法、类.成员]
    </功能详细描述></减法运算并保留两位有效数字> */
    fun subtract(subt1: String, subt2: String): String {
        val sub1 = BigDecimal(subt1)
        val sub2 = BigDecimal(subt2)
        var result = sub1.subtract(sub2)
        result = result.setScale(2, RoundingMode.HALF_UP)
        return result.toString()
    }

    /**
     * <加法运算并保留两位有效数字> <功能详细描述>
     *
     * @param addend1
     * @param addend2
     * @return String
     * @see [类、类.方法、类.成员]
    </功能详细描述></加法运算并保留两位有效数字> */
    fun add(addend1: String, addend2: String): String {
        val add1 = BigDecimal(addend1)
        val add2 = BigDecimal(addend2)
        var result = add1.add(add2)
        result = result.setScale(2, RoundingMode.HALF_UP)
        return result.toString()
    }

    /**
     * <乘法运算并保留两位有效数字> <功能详细描述>
     *
     * @param factor1 String
     * @param factor2 String
     * @return String
     * @see [类、类.方法、类.成员]
    </功能详细描述></乘法运算并保留两位有效数字> */
    fun multiply(factor1: String, factor2: String): String {
        val fac1 = BigDecimal(factor1)
        val fac2 = BigDecimal(factor2)
        var result = fac1.multiply(fac2)
        result = result.setScale(2, RoundingMode.HALF_UP)
        return result.toString()
    }

    /**
     * <除法运算并保留两位有效数字> <功能详细描述>
     *
     * @param divisor1 String
     * @param divisor2 String
     * @return String
     * @see [类、类.方法、类.成员]
    </功能详细描述></除法运算并保留两位有效数字> */
    fun divide(divisor1: String, divisor2: String): String {
        val div1 = BigDecimal(divisor1)
        val div2 = BigDecimal(divisor2)
        val result = div1.divide(div2, 2, RoundingMode.HALF_UP)
        return result.toString()
    }

    /**
     * <除法运算并保留两位有效数字> <功能详细描述>
     *
     * @param divisor1 String
     * @param divisor2 String
     * @return String
     * @see [类、类.方法、类.成员]
    </功能详细描述></除法运算并保留两位有效数字> */
    fun dividePoint1(divisor1: String, divisor2: String): String {
        val div1 = BigDecimal(divisor1)
        val div2 = BigDecimal(divisor2)
        val result = div1.divide(div2, 1, RoundingMode.HALF_UP)
        return result.toString()
    }

    /**
     * <将YYYYMMDDHHmmss 转换为 YYYY-MM-DD> <功能详细描述>
     *
     * @param str
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></将YYYYMMDDHHmmss> */
    fun splitTodate(str: String): String? {
        if (isNullOrZeroLenght(str) || str.length != 14) {
            return str
        }

        var strs = ""
        strs = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8)
        return strs
    }

    /**
     * <将YYYYMMDDHHmmss 转换为 YYYY-MM-DD hh:mm> <功能详细描述>
     *
     * @param str
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></将YYYYMMDDHHmmss> */
    fun splitToMinute(str: String): String? {
        if (isNullOrZeroLenght(str) || str.length != 14) {
            return str
        }

        var strs = ""
        strs = (str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8) + " " + str.substring(8, 10)
                + ":" + str.substring(10, 12))
        return strs
    }

    /**
     * <将YYYY-MM-DD 转换为 YYYYMMDD> <功能详细描述>
     *
     * @param str
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></将YYYY-MM-DD> */
    fun splitToMinuteNoLine(str: String): String {
        if (!str.contains("-")) {
            return str
        }
        var strs = ""
        val strsArr = str.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (strsArr[1].length == 1) {
            strsArr[1] = "0" + strsArr[1]
        }

        if (strsArr[2].length == 1) {
            strsArr[2] = "0" + strsArr[2]
        }

        strs = strsArr[0] + strsArr[1] + strsArr[2]
        return strs
    }

    /**
     * <将YYYYMMDDHHmmss 转换为 YYYY-MM-DD hh:mm:ss> <功能详细描述>
     *
     * @param str
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></将YYYYMMDDHHmmss> */
    fun splitToSecond(str: String): String? {
        if (isNullOrZeroLenght(str) || str.length != 14) {
            return str
        }

        var strs = ""
        strs = (str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8) + " " + str.substring(8, 10)
                + ":" + str.substring(10, 12) + ":" + str.substring(12, 14))
        return strs
    }

    /**
     * <将YYYYMMDDHHmmss 转换为 YY-MM-DD hh:mm:ss> <功能详细描述>
     *
     * @param str
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></将YYYYMMDDHHmmss> */
    fun splitToYear(str: String): String? {
        if (isNullOrZeroLenght(str) || str.length != 14) {
            return str
        }

        var strs = ""
        strs = (str.substring(2, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8) + " " + str.substring(8, 10)
                + ":" + str.substring(10, 12) + ":" + str.substring(12, 14))
        return strs
    }


    /**
     * 获取版本信息
     *
     * @return
     * @throws Exception
     */
    fun getVersionName(context: Context): String {
        try {
            val packageManager = context.packageManager
            val packInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packInfo.versionName
        } catch (e: NameNotFoundException) {
        }

        return ""
    }

    /**
     * <邮箱判断>
     * <功能详细描述>
     *
     * @param email
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></邮箱判断> */
    fun isEmail(email: String): Boolean {
        val str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        val p = Pattern.compile(str)
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * <手机号码判断>
     *
     * @param tel
     * @return
     * @see [类、类.方法、类.成员]
    </手机号码判断> */
    fun isTel(tel: String): Boolean {
        val str = "^[0-9]{11}$"
        val p = Pattern.compile(str)
        val m = p.matcher(tel)
        return m.matches()
    }

    /**
     * <邮编判断>
     * <功能详细描述>
     *
     * @param post
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></邮编判断> */
    fun isPost(post: String): Boolean {
        val patrn = "^[1-9][0-9]{5}$"
        val p = Pattern.compile(patrn)
        val m = p.matcher(post)
        return m.matches()
    }

    /**
     * <密码规则判断>
     * <功能详细描述>
     *
     * @param password
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></密码规则判断> */
    fun IsPassword(password: String): Boolean {
        val str = "^[A-Za-z0-9_]{6,20}$"
        val p = Pattern.compile(str)
        val m = p.matcher(password)
        return m.matches()
    }

    /**
     * <密码位数判断>
     * <功能详细描述>
     *
     * @param password
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></密码位数判断> */
    fun IsPasswordDigit(password: String): Boolean {
        val str = "^[^ ]{6,20}$"
        val p = Pattern.compile(str)
        val m = p.matcher(password)
        return m.matches()
    }

    /**
     * <密码位数判断>
     * <功能详细描述>
     *
     * @param certificate
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></密码位数判断> */
    fun Iscertificate(certificate: String): Boolean {
        val str = "[0-9]{17}([0-9]|[xX])"
        val p = Pattern.compile(str)
        val m = p.matcher(certificate)
        return m.matches()
    }

    /**
     * <获取imei>
     * <功能详细描述>
     *
     * @param context
     * @return
     * @see [类、类.方法、类.成员]
    </功能详细描述></获取imei> */
//    fun getDeviceId(context: Context): String {
//        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        return telephonyManager.getDeviceId()
//    }

    /**
     * http://stackoverflow.com/questions/3495890/how-can-i-put-a-listview-into-a-scrollview-without-it-collapsing/3495908#3495908
     *
     * @param listView
     */
    fun setListViewHeightBasedOnChildrenExtend(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        val desiredWidth = MeasureSpec.makeMeasureSpec(listView.width, MeasureSpec.AT_MOST)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.count) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0) {
                view!!.layoutParams = LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT)
            }
            view!!.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

    // 去除textview的排版问题
    fun ToDBC(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i].toInt() == 12288) {
                c[i] = 32.toChar()
                continue
            }
            if (c[i].toInt() > 65280 && c[i].toInt() < 65375)
                c[i] = (c[i].toInt() - 65248).toChar()
        }
        return String(c)
    }

    /**
     * 返回当前程序版本名
     */
    fun getAppVersionName(context: Context): String {
        var versionName = ""
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            versionName = pi.versionName
        } catch (e: Exception) {
        }

        return versionName
    }


    /**
     * @param context
     * @return
     */
//    fun getMachineId(context: Context): String {
//        var tmDevice = ""
//        try {
//            if (SharedPreferencesManager[context, BaseConstants.MACHINE_ID, ""] == "") {
//                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
////                tmDevice =""
//                tmDevice = tm.deviceId;
//                SharedPreferencesManager.put(context, BaseConstants.MACHINE_ID, tmDevice)
//            } else {
//                tmDevice = SharedPreferencesManager.get(context, BaseConstants.MACHINE_ID, "") as String
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            tmDevice = (System.currentTimeMillis().toString() + "").hashCode().toString() + ""
//            SharedPreferencesManager.put(context, BaseConstants.MACHINE_ID, tmDevice)
//        }
//
//        return tmDevice
//    }

    /**
     * 进行获取手机唯一标识!
     * @param context
     * @return
     */
//    fun getMachineFromSP(context: Context): String {
//        var machineID: String = SharedPreferencesManager.get(context, BaseConstants.MACHINE_ID, "") as String
//        if (machineID == "") {
//            machineID = getMachineId(context)
//            SharedPreferencesManager.put(context, BaseConstants.MACHINE_ID, machineID)
//        }
//        return machineID
//    }


    /**
     * 进行解压 压缩文件
     */
    fun unZip() {

    }

    /**
     * 进行判断是否包含 对应的目录
     * @param dir
     * @return
     */
    fun containsDir(dir: String): Boolean {
        return false
    }


    /**
     * 检测文件是否存在
     * @param communicationPic
     * @param simulationId
     * @throws ContentException
     */
     fun checkFileExist(communicationPic: String?, simulationId: String) {
        if (communicationPic != null && communicationPic != "") {
            var file: File? = File(communicationPic)
            if (!file!!.exists()) {
                CrashHandler.INSTANCE.saveActionLog2File("$simulationId checkFileExist $communicationPic.....文件不存在!")
                //及时释放资源!
                file = null
                throw ContentException("试题资源缺失!")
            }
        }
    }


    /**
     * 通过四级或者六级状态,compatId 来获取标题的名称
     * （四级）1 自我介绍  2 短文朗读与简短问答  3 个人陈述   4 两人互动
     * （六级）1 自我介绍  2 简短问答  3 陈述   4 讨论  5 问答
     * @param siOrSix
     * @param compatId
     * @return
     */
    fun getCompatExampleNameBy(siOrSix: String, compatId: Int): String {
        var combatName = ""
        if (siOrSix == BaseConstants.CLASS_FOUR_JI) {
            //四级状态
            when (compatId) {
                1 -> combatName = "自我介绍"
                2 -> combatName = "短文朗读与简短问答"
                3 -> combatName = "个人陈述"
                4 -> combatName = "两人互动"
            }
        } else {
            //六级状态
            when (compatId) {
                1 -> combatName = "自我介绍"
                2 -> combatName = "简短问答"
                3 -> combatName = "陈述"
                4 -> combatName = "讨论"
                5 -> combatName = "问答"
            }
        }
        return combatName
    }

    /**
     * 通过秒进行返回 对应的00:00格式的时间
     * @param seconds
     * @return
     */
    fun getTimeString(seconds: Long): String {
        val time = (seconds / 1000).toInt()
        val stringBuilder = StringBuilder()
        if (time < 10) {
            stringBuilder.append("00:").append(0).append(time)
        } else if (time < 60) {
            stringBuilder.append("00:").append(time)
        } else if (time < 600) {
            stringBuilder.append(0).append(time / 60)
            if (time % 60 < 10) {
                stringBuilder.append(":").append(0).append(time % 60)
            } else {
                stringBuilder.append(":").append(time % 60)
            }
        } else {
            stringBuilder.append(time / 60)
            if (seconds % 60 < 10) {
                stringBuilder.append(":").append(0).append(time % 60)
            } else {
                stringBuilder.append(":").append(time % 60)
            }
        }
        return stringBuilder.toString()
    }


    /**
     * 进行获取比例值 [0,1000]
     * @param current
     * @param start
     * @param end
     * @return
     */
    @Throws(ContentException::class)
    fun getLower100(current: Long, start: Long, end: Long): Int {
        if (start > end) {
            throw ContentException()
        } else if (current < start - BaseConstants.XIAOMI_TIME_WUCHA || current > end) {
            throw ContentException()
        }
        val value = (current - start).toDouble() / (end - start).toDouble()
        return (value * BaseConstants.MAX_PROGRESS).toInt()
    }

    /**
     * 进行获取剩余的时间
     * @param current
     * @param start
     * @param end
     * @return
     * @throws ContentException
     */
    @Throws(ContentException::class)
    fun getLeftTime(current: Long, start: Long, end: Long): Int {
        if (start > end) {
            throw ContentException()
        } else if (current < start - BaseConstants.XIAOMI_TIME_WUCHA || current > end) {
            throw ContentException()
        }
        val value = (current - start).toDouble() / (end - start).toDouble()
        return ((1 - value) * (end - start).toDouble() * 0.001).toInt() + 1
    }


    /**
     * 进行获取问题的序号
     * @param number
     * @return
     */
    fun getQuestionNumber(number: Int): String {
        return BaseConstants.QUESTION_STRING + number + "："
    }

    fun showMoniDialog(context: Context, listener: MoniTipsDialog.CustomDialogListener, isSingleBtn: Boolean): MoniTipsDialog {
        moniTipsDialog = MoniTipsDialog(context)
        moniTipsDialog!!.setCancelable(false)
        moniTipsDialog!!.listener=(listener)
        moniTipsDialog!!.isSingleBtn=(isSingleBtn)
        return moniTipsDialog!!
    }

    fun showMoniDialog(context: Context, listener: MoniTipsDialog.CustomDialogListener?,
                                                tipsContent: String, isSingleBtn: Boolean): MoniTipsDialog {
        moniTipsDialog2 = MoniTipsDialog(context, R.style.customDialog, tipsContent, isSingleBtn)
        moniTipsDialog2!!.setCancelable(false)
        moniTipsDialog2!!.listener=(listener)
        return moniTipsDialog2!!
    }

    fun showMoniDialog(context: Context, listener: MoniTipsDialog.CustomDialogListener,
                                    tipsContent: String, strOk: String, strCancel: String): MoniTipsDialog {
        moniTipsDialog2 = MoniTipsDialog(context, R.style.customDialog, tipsContent)
        moniTipsDialog2!!.setCancelable(false)
        moniTipsDialog2!!.str_ok=strOk
        moniTipsDialog2!!.str_repipei=(strCancel)
        moniTipsDialog2!!.listener=(listener)
        return moniTipsDialog2!!

    }

    /***
     * 进行获取云信相关声音的level值
     * @param volumeVale
     * @return
     */
    fun getYunxinVolumeLevel(volumeVale: Int): Int {
        return if (volumeVale <= BaseConstants.YUNXIN_VOLUME_START_VALUE) {
            0
        } else if (volumeVale <= BaseConstants.YUNXIN_VOLUME_CHAZHI * 1) {
            1
        } else if (volumeVale <= BaseConstants.YUNXIN_VOLUME_CHAZHI * 2) {
            2
        } else if (volumeVale <= BaseConstants.YUNXIN_VOLUME_CHAZHI * 3) {
            3
        } else if (volumeVale <= BaseConstants.YUNXIN_VOLUME_CHAZHI * 4) {
            4
        } else if (volumeVale <= BaseConstants.YUNXIN_VOLUME_CHAZHI * 5) {
            5
        } else if (volumeVale <= BaseConstants.YUNXIN_VOLUME_CHAZHI * 6) {
            6
        } else if (volumeVale <= BaseConstants.YUNXIN_VOLUME_CHAZHI * 7) {
            7
        } else {
            8
        }
    }

    /**
     * 合并两个指定文件的目录
     * @param mp3Dir
     * @param mp3Dir1
     * @return
     */
    fun fixFile(mp3Dir: String, mp3Dir1: String): String {
        return ""
    }


    /**
     * 合并文件2
     * @param fileList
     */
    fun fixFils2(fileList: List<File>, fileOutPath: String) {
        //先进行获取文件长度 和音频文件长度 写入到Head byte数组中!
        val length = LongArray(4)
        var totalLength: Long = 0
        for (i in fileList.indices) {
            length[i] = fileList[i].length() - 44//每个文件长度-44
            totalLength += length[i]
        }
        //设置总长度:
        val byteBuffer = ByteBuffer.allocate(4)//整型为4个字节
        byteBuffer.putInt(Integer.parseInt(totalLength.toString() + "") + 36)
        var fileLength = byteBuffer.array()//获取文件长度

        val byteBuffer1 = ByteBuffer.allocate(4)
        byteBuffer1.putInt(Integer.parseInt(totalLength.toString() + ""))
        var dataLength = byteBuffer1.array()//音频数据长度

        fileLength = reverse(fileLength)  //数组反转
        dataLength = reverse(dataLength)

        val head = ByteArray(44)
        val byteArrayOutputStream = ByteArrayOutputStream()
        var len = -1
        val buffer = ByteArray(1024)
        //进行获取头部信息:
        try {
            for (i in fileList.indices) {
                //
                val fileInputStream = FileInputStream(fileList[i])
                if (i == 0) {
                    //读取第一个文件头的信息
                    fileInputStream.read(head, 0, head.size)
                    for (j in 0..3) {  //4代表一个int占用字节数
                        head[j + 4] = fileLength[j]  //替换原头部信息里的文件长度
                        head[j + 40] = dataLength[j]  //替换原头部信息里的数据长度
                    }
                    //先将头文件 写进去！
                    byteArrayOutputStream.write(head, 0, head.size)
                    len = fileInputStream.read(buffer, 0, buffer.size)
                    while ( len!= -1) {
                        byteArrayOutputStream.write(buffer, 0, len)
                        len = fileInputStream.read(buffer, 0, buffer.size)
                    }
                    fileInputStream.close()
                } else {
                    fileInputStream.read(buffer, 0, 44)
                    len = fileInputStream.read(buffer, 0, buffer.size)
                    while (len != -1) {
                        byteArrayOutputStream.write(buffer, 0, len)
                        len = fileInputStream.read(buffer, 0, buffer.size)
                    }
                    fileInputStream.close()
                }
            }

            //进行写出文件:
            val fileOutputStream = FileOutputStream(fileOutPath)
            byteArrayOutputStream.writeTo(fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * 数组反转
     * @param array
     */
    fun reverse(array: ByteArray): ByteArray {
        var temp: Byte
        val len = array.size
        for (i in 0 until len / 2) {
            temp = array[i]
            array[i] = array[len - 1 - i]
            array[len - 1 - i] = temp
        }
        return array
    }
}
