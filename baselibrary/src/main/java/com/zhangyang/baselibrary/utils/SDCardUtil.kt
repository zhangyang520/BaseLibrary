package com.zhangyang.baselibrary.utils

import android.os.Environment
import com.jianhua.zcode.assets.baselibrary.UiUtils
import com.zhangyang.baselibrary.common.BaseConstants
import com.zhangyang.baselibrary.exception.ContentException
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


/**

 * @项目名：AroundYou
 * *
 * @类名称：SDCardUtil
 * *
 * @类描述：   对SD卡的操作
 * *
 * @创建人：huangxianfeng
 * *
 * @修改人：
 * *
 * @创建时间：2016-7-19 下午5:11:52
 * *
 * @version
 */
class SDCardUtil private constructor() {
    private val AppRootPath: String
    /**
     * 进行获取上传目录的路径
     * @return
     */
    //创建noMedia文件
    var uploadPic: String? = null
        get() {
            try {
                val noMediaFile = File(field, ".nomedia")
                if (!noMediaFile.exists()) {
                    noMediaFile.createNewFile()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            makeDirs(field!!)
            return field
        }
    /**
     * 进行获取图片缓存的路径
     * @return
     */
    var picCache: String? = null
        get() {
            makeDirs(field!!)
            return field
        }
    /**
     * 进行获取放置的 zip 试题的目录
     * @return
     */
    var zipDir: String? = null
        get() {
            makeDirs(field!!)
            return field
        }
    /**
     * 进行获取下载的路径
     * @return
     */
    var downloadDir: String? = null
        get() {
            makeDirs(field!!)
            return field
        }
    /**
     * 进行获取录音的目录
     * @return
     */
    //创建noMedia文件
    var recordPath: String? = null
        get() {
            try {
                val noMediaFile = File(field, ".nomedia")
                if (!noMediaFile.exists()) {
                    noMediaFile.createNewFile()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            makeDirs(field!!)
            return field
        }
    private var debugPath: String? = null
    private val newTaskPath: String

    init {
        //应用程序根目录
        AppRootPath = rootPath
        //对应的存储目录
        this.uploadPic = AppRootPath + File.separator + BaseConstants.uploadPic
        this.picCache = AppRootPath + File.separator + BaseConstants.picCache
        this.zipDir = AppRootPath + File.separator + BaseConstants.zipDir
        this.downloadDir = AppRootPath + File.separator + BaseConstants.downloadDir
        this.recordPath = AppRootPath + File.separator + BaseConstants.recordPathDir
        debugPath = AppRootPath + File.separator + BaseConstants.debugPath
        debugPath = AppRootPath + File.separator + BaseConstants.debugPath
        newTaskPath = AppRootPath + File.separator + BaseConstants.newTaskPath
    }


    /**
     * 检测SD卡是否存在
     * @return
     */
    ////System.out.println("isExitsSdcard ...true");
    ////System.out.println("isExitsSdcard ...false");
    val isExitsSdcard: Boolean
        get() {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                return true
            } else {
                return false
            }
        }

    /**
     * 通过指定目录创建文件夹
     * @param dirPath
     */
    fun makeDirs(dirPath: String) {
        val file = File(dirPath)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 获取系统默认存储目录
     * @return
     */
    // 允许 sdcard的目录
    // 直接是内置存储卡的目录
    ////System.out.println("getRootPath ..."+filepath);
    val rootPath: String
        get() {
            var filepath = ""
            if (BaseConstants.IS_SD_CAN) {
                if (isExitsSdcard)
                    filepath = sdCardPath + File.separator + BaseConstants.ROOTPATH + File.separator
                else {
                    filepath = rootDirectoryPath + File.separator + BaseConstants.ROOTPATH + File.separator
                }
            } else {
                filepath = rootDirectoryPath + File.separator + BaseConstants.ROOTPATH + File.separator
            }
            val f = File(filepath)
            if (!f.exists() || !f.isDirectory) {
                f.mkdirs()
            }
            return filepath
        }

    private val sdCardPath: String
        get() = Environment.getExternalStorageDirectory().absolutePath + File.separator

    /**

     * @return
     * * 获取系统存储路径
     */
    private val rootDirectoryPath: String
        get() = UiUtils.instance.getContext().getFilesDir().getAbsolutePath()

    /**

     * @描述:根据对应的路径读取txt文件
     * *
     * @方法名：getJsonDataForSD
     * *
     * @创建时间：2015-9-16 下午4:33:32
     */
    fun getJsonDataForSD(jsonPath: String): String {
        ////System.out.println("getJsonDataForSD jsonPath:"+jsonPath);
        var file: File? = File(jsonPath)
        var read: BufferedReader? = null
        if (file!!.exists() && file.isFile) {
            val stringBuilder = StringBuilder()
            try {
                read = BufferedReader(FileReader(file))
                var str: String? = read.readLine()
                while (str!= null) {
                    stringBuilder.append(str)
                    str= read.readLine()
                }
                return stringBuilder.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                return BaseConstants.STRING_GAP
            } finally {
                if (read != null) {
                    try {
                        read.close()
                        read = null
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        } else {
            ////System.out.println("jsonPath:"+jsonPath+"..file exist:"+file.exists()+"..isFile:"+file.isFile());
            file = null
            return BaseConstants.STRING_GAP
        }
    }

    /**
     * 进行获取上传目录的路径
     * @return
     */
    fun getDebugPath(): String {
        makeDirs(debugPath!!)
        return debugPath as String
    }

    fun getNewTaskPath(): String {
        makeDirs(newTaskPath)
        return newTaskPath
    }

    /**
     * 通过路径获取文件的对象
     * @param filePath
     * *
     * @return
     */
    @Throws(ContentException::class)
    fun getFileByPath(filePath: String): File {
        val file = File(filePath)
        if (!file.exists()) {
            throw ContentException()
        }
        return file
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * *
     * @return boolean Returns "true" if all deletions were successful.
     * *                 If a deletion fails, the method stops attempting to
     * *                 delete and returns "false".
     */
    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            //递归删除目录中的子目录下
            for (i in children!!.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        if(dir.exists()){
            // 目录此时为空，可以删除
            return dir.delete()
        }
       return false
    }

    companion object {
         val instance = SDCardUtil()
    }
}

