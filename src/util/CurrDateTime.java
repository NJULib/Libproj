package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ����ϵͳ�ĵ�ǰ���ں�ʱ�䣬
 * ʱ�侫ȷ�����롣
 */
public class CurrDateTime {

	/**
	 * ��̬����������ϵͳ��ǰʱ��
	 */

	public static String currTime(){
		return new SimpleDateFormat("[hh:mm:ss:SSS] ").format(new Date());		
	}
	
	/**
	 * ��̬����������ϵͳ��ǰ������
	 */
	public static String currDate(){
		return new SimpleDateFormat("[yyyy-MM-dd] ").format(new Date());		
	}
	
	/**
	 * ��̬����������ϵͳ��ǰ�����ں�ʱ��
	 */
	public static String currDateTime(){
		return new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss:SSS] ").format(new Date());		
	}
	
	public static String myDateTime(String datetimeforamt){
		return new SimpleDateFormat(datetimeforamt).format(new Date());		
	}
}
