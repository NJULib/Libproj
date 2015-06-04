package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DaysInterval {
	public DaysInterval(){}
	/** 1.计算两个日期之间相差的天数      
     *@param strDate1 2009-01-12
	 * @param strDate2 2009-10-9
	 * 较大的时间after较小的时间，即2009-10-9.after(2009-01-12)为true
	 * 
	 */
	public static long iDays(String strDate1,String strDate2){
		long betweenDays = 0;
		try{
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(strDate1);		
			Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(strDate2);
			//System.out.println("转换前：date2:"+date2+"date1:"+date1);
			/**
			 * date2时间值大，两者交换，使date1>date2
			 */
			if(date2.after(date1)){
				Date temp = date1;
				date1 = date2;
				date2 = temp;								
			}
			betweenDays = getDays(date1,date2);
		}catch(ParseException e){
			System.out.println("将字符串转换成日期类型失败");
			betweenDays = 0;
		}
		return betweenDays;
	}
	/**
	 * 2.若传入的是日期类型的话，直接调用
	 * date1=new Date（）
	 */
	public static int getDays(Date date1,Date date2){
		int days = 0;
		if(date1.before(date2)){
			days=  0;
		}else{
			days = (int)((date1.getTime()-date2.getTime())/ (1000 * 60 * 60 *24) + 0.5);
		}
		return days;
	}


}
