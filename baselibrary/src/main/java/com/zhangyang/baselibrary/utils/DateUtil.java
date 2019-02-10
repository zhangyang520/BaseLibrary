package com.zhangyang.baselibrary.utils;

import com.lidroid.xutils.util.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * @author lvxile
 *
 */
public class DateUtil {
	/**
	 * 返回当前时间的long格式
	 * @return
	 */
	public static String getDateLong(){
		return String.valueOf(Calendar.getInstance().getTimeInMillis());
	}
	
	public static String dateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().getTime());
	}
	
	public static String getCurrentDateTime(){
		return "["+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(new Date(System.currentTimeMillis()))+"]";
	}
	
	public static String getCurrentTime(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(new Date());
	}


	public static String dateFormatMonth(Date date){
		return new SimpleDateFormat("yyyy-MM",Locale.getDefault()).format(date);
	}

	public static String dateFormat(Date date){
		return new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).format(date);
	}

	public static String dateFormat3(Date date){
		return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(date);
	}
	
	public static String dateFormat2(Date date){
		return new SimpleDateFormat("yyyy年-MM月-dd日  HH:mm:ss",Locale.getDefault()).format(date);
	}
	public static Date parse2Date(String s){
		try {
			return new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			LogUtils.e("日期格式不正确，无法转换");
			return Calendar.getInstance(Locale.getDefault()).getTime();
		}
	}

	/**
	 * 解析 月份 格式的日期
	 * @param s
	 * @return
	 */
	public static Date parse2DateMonth(String s){
		try {
			return new SimpleDateFormat("yyyy-MM",Locale.getDefault()).parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			LogUtils.e("日期格式不正确，无法转换");
			return Calendar.getInstance(Locale.getDefault()).getTime();
		}
	}
	
	public static Date parse2DateTime(String s){
		try {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault()).parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			LogUtils.e("日期格式不正确，无法转换");
			return Calendar.getInstance(Locale.getDefault()).getTime();
		}
	}
	
	
	/**
	 * 
	 * @描述:得到固定格式的月和日2015-07-13
	 * @param time
	 * @return 
	 */
	public static String getYearOrMonthOrDay(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date(time));
	}
	
}
