package com.zhangyang.baselibrary.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.lidroid.xutils.BitmapUtils
import com.jianhua.zcode.assets.baselibrary.UiUtils
import com.zhangyang.baselibrary.R
import com.zhangyang.baselibrary.utils.SDCardUtil
import java.io.File


class BitmapUtil {

    companion object {
        var instance=BitmapUtil()
        var bitmapUtils: BitmapUtils?=null
        var myInstance: BitmapUtils? = null
        var advInstance: BitmapUtils? = null
    }

    init {
        bitmapUtils =BitmapUtils(UiUtils.instance.getContext(), SDCardUtil.instance.picCache);
        /***给显示图片的ImagView加载失败设置缺省图片**/
        bitmapUtils?.configDefaultLoadFailedImage(R.drawable.shipin);
        /***给显示图片的ImagView正在加载中设置的缺省图片**/
        bitmapUtils?.configDefaultLoadingImage(R.drawable.shipin);

        //我的界面
        myInstance = BitmapUtils(UiUtils.instance.getContext(), SDCardUtil.instance.picCache)
        /***给显示图片的ImagView加载失败设置缺省图片**/
        myInstance?.configDefaultLoadFailedImage(R.drawable.moren);
        /***给显示图片的ImagView正在加载中设置的缺省图片**/
        myInstance?.configDefaultLoadingImage(R.drawable.moren);
        myInstance?.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888);


        advInstance = BitmapUtils(UiUtils.instance.getContext(), SDCardUtil.instance.picCache)
        /***给显示图片的ImagView加载失败设置缺省图片**/
        advInstance?.configDefaultLoadFailedImage(R.drawable.shouye);
        /***给显示图片的ImagView正在加载中设置的缺省图片**/
        advInstance?.configDefaultLoadingImage(R.drawable.shouye);
        advInstance?.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888);
    }


    /**
     * @param context
     * @param resId
     * @param width   改变后的图片像素宽度
     * @param height  改变后的图片像素高度
     * @return 返回指定大小的位图
     */


    fun getAbsImage(context: Context, resId: Int, width: Int,
                    height: Int): Bitmap {

        val opts = BitmapFactory.Options()
        // 不解析像素信息，只解析图片宽高，那么就不会申请内存去保存像素信息
        opts.inJustDecodeBounds = true
        val `is` = context.getResources().openRawResource(resId)
        // api会解析图片的所有像素信息，把像素信息保存在内存中
        BitmapFactory.decodeStream(`is`, null, opts)

        opts.inPreferredConfig = Bitmap.Config.ARGB_8888
        opts.outHeight = height
        opts.outWidth = width
        // opts.inSampleSize = 大的图片宽 / 小的图片宽 500 / 100 = 5 那么就是5分之一
        opts.inJustDecodeBounds = false
        // 按照缩小后的比例来解析像素

        return BitmapFactory.decodeStream(`is`, null, opts)

    }


    /**
     * 一个快速减少图片占用内存的方法~
     *
     * @param context
     * @param resId   资源id
     * @param scale   缩小为原图片的多少倍,比如该值为4 ,这该图片为原图片的1/4
     * @return
     * @author zh
     */


    fun readBitMap(context: Context, resId: Int, scale: Int): Bitmap {
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.RGB_565
        opt.inPurgeable = true
        opt.inInputShareable = true
        opt.inSampleSize = Math.sqrt(scale.toDouble()).toInt()
        // 获取资源图片
        val `is` = context.getResources().openRawResource(resId)
        return BitmapFactory.decodeStream(`is`, null, opt)
    }

    /**
     * @param options   位图的宽高信息等
     * @param reqWidth  希望得到的位图宽
     * @param reqHeight 希望得到的位图高
     * @return
     */


    fun calculateInSampleSize(options: BitmapFactory.Options,
                              reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round(height.toFloat() / reqHeight.toFloat())
            } else {
                inSampleSize = Math.round(width.toFloat() / reqWidth.toFloat())
            }
        }
        return inSampleSize
    }

    /**
     * 进行展示图片
     *
     * @param imageView
     * @param url
     */
    //首页轮播图的网页请求
    fun displayImg(imageView: ImageView, url: String) {
        bitmapUtils?.configDefaultLoadFailedImage(R.drawable.shipin)
        /***给显示图片的ImagView正在加载中设置的缺省图片 */
        bitmapUtils?.configDefaultLoadingImage(R.drawable.shipin)
        bitmapUtils?.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888)
        bitmapUtils?.display(imageView, url)
    }


    /**
     * 头像
     *
     * @param imageView
     * @param url
     */

    fun displayImgPic(imageView: ImageView, url: String) {
        bitmapUtils?.display(imageView, url)
        bitmapUtils?.configDefaultLoadFailedImage(R.drawable.moren)
        /***给显示图片的ImagView正在加载中设置的缺省图片 */
        bitmapUtils?.configDefaultLoadingImage(R.drawable.moren)
    }

    //我的页面，头像的图片请求
    fun displayUserPic(imageView: ImageView, url: String) {
        /***给显示图片的ImagView加载失败设置缺省图片 */
        myInstance?.configDefaultLoadFailedImage(R.drawable.moren)
        /***给显示图片的ImagView正在加载中设置的缺省图片 */
        myInstance?.configDefaultLoadingImage(R.drawable.moren)
        myInstance?.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888)
        myInstance?.display(imageView, url, true)
    }

    //广告页的网络请求图片-默认背资源图shouye
    fun displayUserAdv(imageView: ImageView, url: String) {
        /***给显示图片的ImagView加载失败设置缺省图片 */
        advInstance?.configDefaultLoadFailedImage(R.drawable.shouye)
        /***给显示图片的ImagView正在加载中设置的缺省图片 */
        advInstance?.configDefaultLoadingImage(R.drawable.shouye)
        advInstance?.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888)
        advInstance?.display(imageView, url)

    }

    fun displayImg(imageView: ImageView, path: String, failedImgId: Int) {
        bitmapUtils?.configDefaultLoadFailedImage(failedImgId)
        /***给显示图片的ImagView正在加载中设置的缺省图片 */
        bitmapUtils?.configDefaultLoadingImage(failedImgId)
        bitmapUtils?.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888)
        bitmapUtils?.display(imageView, path)
    }

    fun clearCache(url: String) {
        bitmapUtils?.clearMemoryCache(url)
        bitmapUtils?.clearCache(url)
        bitmapUtils?.clearDiskCache(url)
    }

    /**
     * 得到本地磁盘当中缓存的图片文件（可以判断缓存的文件是否存在）
     *
     * @param url
     * @return
     */

    fun getBitmapFileCache(url: String): File? {
        return bitmapUtils?.getBitmapFileFromDiskCache(url)
    }
}