package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 返回系统的当前日期和时间，
 * 时间精确到毫秒。
 */
public class CurrDateTime {

	/**
	 * 静态方法，返回系统当前时间
	 */

	public static String currTime(){
		return new SimpleDateFormat("[hh:mm:ss:SSS] ").format(new Date());		
	}
	
	/**
	 * 静态方法，返回系统当前的日期
	 */
	public static String currDate(){
		return new SimpleDateFormat("[yyyy-MM-dd] ").format(new Date());		
	}
	
	/**
	 * 静态方法，返回系统当前的日期和时间
	 */
	public static String currDateTime(){
		return new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss:SSS] ").format(new Date());		
	}
	
	public static String myDateTime(String datetimeforamt){
		return new SimpleDateFormat(datetimeforamt).format(new Date());		
	}
}
