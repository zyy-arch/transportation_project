package com.tsAdmin.common;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
 

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class tools {
	
	
    private static boolean isEmojiCharacter(char codePoint) { 
    	return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)               
    			|| (codePoint == 0xD)           
    			|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))              
    			|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))             
    			|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));   
    	}    
    /**     * 过滤emoji 或者 其他非文字类型的字符     *     * @param source     * @return     */    
    public static String filterEmoji(String source) {  
    	
    	if (StringUtils.isBlank(source)) {            return source;        }      
    	StringBuilder buf = null;      
    	int len = source.length();       
    	for (int i = 0; i < len; i++) {       
    		char codePoint = source.charAt(i);          
    		if (isEmojiCharacter(codePoint)) {              
    			if (buf == null) {                  
    				buf = new StringBuilder(source.length());              
    				}              
    			buf.append(codePoint);            }        }      
    	if (buf == null) {            return source;        } 
    	else {           
    		if (buf.length() == len) {               
    			buf = null;               
    			return source;           
    			} else {             
    				return buf.toString();        
    				}       
    		}
    }

 
	/**
	 * 插入一条验证码
	 * @param vaild_code
	 * @param is_used
	 * @param type
	 * @param addDate
	 * @return
	 */
	public static int insertVaildCode(String vaild_code,String is_used,String type,String para,String addDate)
	{
      Record record=new Record();
	record.set("vaild_code",vaild_code).set("is_used",is_used).set("type",type).set("addDate",addDate).set("para", para);
	 Db.save("t_sys_vaildcode", record);
	 return Integer.parseInt(record.get("id").toString());

	}
	/**
	 * 计算两个时间之差
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getGapCount(String start, String end) {
		
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date startDate = null;
         Date endDate = null;
	      try {
	    	  startDate = sdf.parse(start);
	    	  endDate = sdf.parse(end);
	      } catch (java.text.ParseException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	      }
		
		
        Calendar fromCalendar = Calendar.getInstance(); 
        fromCalendar.setTime(startDate); 
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0); 
        fromCalendar.set(Calendar.MINUTE, 0); 
        fromCalendar.set(Calendar.SECOND, 0); 
        fromCalendar.set(Calendar.MILLISECOND, 0); 
 
        Calendar toCalendar = Calendar.getInstance(); 
        toCalendar.setTime(endDate); 
        toCalendar.set(Calendar.HOUR_OF_DAY, 0); 
        toCalendar.set(Calendar.MINUTE, 0); 
        toCalendar.set(Calendar.SECOND, 0); 
        toCalendar.set(Calendar.MILLISECOND, 0); 
 
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}
	/**
	 * 时间加减（秒）
	 * @param DateTime
	 * @param seconds
	 * @return
	 */
	public static String timeChange(String DateTime,int seconds) {
		
		
		DateTime dt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(DateTime);
		 
        return dt.plusSeconds(seconds).toString("yyyy-MM-dd HH:mm:ss");
     
		
		
	}
	
	
	  /**
	   * 转换时区
	   * @param time
	   * @param NowTimeZone
	   * @param TargetTimeZone
	   * @return
	   */
	  public static String timeTransfer(String time, String NowTimeZone, String TargetTimeZone) {
		    if(time.equals("")||time.length()!=19){
		        return "";
		    }
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(NowTimeZone));
		    Date date;
		    try {
		        date = simpleDateFormat.parse(time);
		    } catch (ParseException e) {
		        //LOGGER.info("info", e);
		        return "0";
		    }
		    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TargetTimeZone));
		    return simpleDateFormat.format(date);
		}
	
	/**
	 * 计算百分率,值范围1~100
	 * @param a
	 * @param b
	 * @return
	 */
	public static String calRate(String as,String bs) {
		int a=Integer.parseInt(as);
		int b=Integer.parseInt(bs);
		if(a==0)b=1;
		if(b==0){a=0;b=1;}
		 DecimalFormat df=new DecimalFormat("0.00"); 
		return df.format((float)a/(float)b*100);
	}
	/**
	 * 计算百分率(小数)
	 * @param a
	 * @param b
	 * @return
	 */
	public static String calRate(int a,int b) {
		
		 DecimalFormat df=new DecimalFormat("0.00"); 
		if (b==0) {
			return "0.00";
		}
		return df.format((float)a/(float)b);
	}
	/**
	 * 获得真实IP
	 * @param request
	 * @return
	 */
	public static String getRemortIP(HttpServletRequest request) {

		 if (request.getHeader("x-forwarded-for") == null) {

		 return request.getRemoteAddr(); 

		} 

		return request.getHeader("x-forwarded-for"); 

		} 
	/**
	 * 判断是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
  }
	/**
	 * 比较2个时间（或日期折大小）
	 * @param dateTime1
	 * @param dateTime2
	 * @return dt1大于d2时，返回1，小于返回-1，相等返回0
	 */
	 public static int dateTimeCompare(String dateTime1,String dateTime2) {
		
		 String dt1=dateTime1.replace("-", "");
		 dt1=dt1.replace(" ", "");
		 dt1=dt1.replace(":", "");
		 String dt2=dateTime2.replace("-", "");
		 dt2=dt2.replace(" ", "");
		 dt2=dt2.replace(":", ""); 
		 long l1=Long.parseLong(dt1);
		 long l2=Long.parseLong(dt2);
		 if (l1>l2) {
			return 1;
		}else if(l1<l2)
		{
			return -1;
		}else {
			return 0;
		}
		 
	}
	 /**
		 * 得到指定长度的字串(仅数字)
		 * @param length
		 * @return
		 */
	    public static String getRandomString_num(int length){  
	    	String strSet = "0123456789";  
	        StringBuffer str = new StringBuffer();  
	        Random rd=new Random();
	        while(length > 0){  
	        	int ix=rd.nextInt(10);
	            char ch = strSet.charAt(ix);
	            str.append(ch);  
	            length--;  
	        }  
	        return str.toString();  
	    } 
	/**
	 * 得到指定长度的字串
	 * @param length
	 * @return
	 */
    public static String getRandomString(int length){  
    	String strSet = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";  
        StringBuffer str = new StringBuffer();  
        Random rd=new Random();
        while(length > 0){  
        	int ix=rd.nextInt(62);
            char ch = strSet.charAt(ix);
            str.append(ch);  
            length--;  
        }  
        return str.toString();  
    }  
	/**
	 * 限制小数位置
	 * @param number
	 * @param integerSize 整数长度  0为不限制
	 * @param pointSize  小数长度
	 * @return
	 */
	public static String  decimalFix(String number,int integerSize,int pointSize) {
		String conncetChar=".";
		String v[]=number.split(".");
		if(v.length>=2)
		{
		if(v[0].equals(""))v[0]="0";
		if(v[1].equals(""))v[1]="0";
		if (v[0].length()>integerSize) {
			if(integerSize!=0)
				v[0]=v[0].substring(v[0].length()-integerSize);
		}
		if (v[1].length()>pointSize) {
			v[1]=v[1].substring(0,pointSize);
		}
		return v[0]+conncetChar+v[1];
		}
		else {
			return number;
		}
	}
	
	/**
	 * 得到页码最小值
	 * @param pageNumber
	 * @param range
	 * @return
	 */
	public static int  getPageMin(int pageNumber,int range )
	{	
		return  pageNumber-range>0?pageNumber-range:1;
	}
	/**
	 * 得到页码最大值
	 * @param pageNumber
	 * @param range
	 * @param totalPage
	 * @return
	 */
	public static int  getPageMax(int pageNumber,int range ,int totalPage)
	{	
		return  pageNumber+range>totalPage?totalPage:pageNumber+range;
	}
	
	/**
	 * 最长字符截取
	 * @param Str
	 * @param maxLength
	 * @return
	 */
	public static String  cutStr(String Str ,int maxLength )
	{	
		  if(Str==null)return "";
		  maxLength=maxLength>Str.length()?Str.length():maxLength;
		  return Str.substring(0, maxLength);
	}
	/**
	 * 生成随机数
	 * @param max
	 * @param min
	 * @return
	 */
	public static int  getRandomNumber(int max ,int min )
	{	
		  Random random = new Random();

	        int s = random.nextInt(max)%(max-min+1) + min;
	        
	      return s;
	}
	/**
	 * 将不带毫秒的时间戳转换为文本形式
	 * @param timems
	 * @return
	 */
	public static String getDataString(String timems)
	{
		long ms=Long.parseLong(timems+"000");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString="";
		try {
			timeString=sdf.format(ms);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return timeString;
		
	}
	/**
	 * 获得当前时间
	 * @return
	 */
	public static String  getDataTimeString() {
		long ms=System.currentTimeMillis();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString="";
		try {
			timeString=sdf.format(ms);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return timeString;
	}
	/**
	 * 获得当前日期
	 * @return
	 */
	public static String  getDataString() {
		long ms=System.currentTimeMillis();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString="";
		try {
			timeString=sdf.format(ms);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return timeString.substring(0,10);
	}
	/**
	 *
	 * @param response 响应
	 * @param request   请求
	 * @param param 为空的字段
	 */
	public static void outEmpty(HttpServletResponse response, HttpServletRequest request, String param) {
	    try {
	        response.setHeader("Content-Type", "application/json;charset=UTF-8"); //设置返回头是json格式
	        PrintWriter writer = response.getWriter();
	        request.getInputStream().close();
	        writer.println(param + " can not null");  //输出错误信息，我这里将其封装了起来 封装了一个JSON的数据
	        writer.flush();
	        writer.close();
	    } catch (Exception e) {
	    }
	}
	/**
	 * 获得当前操作用户ID
	 * @param c
	 * @return
	 */
	public static String getSessionStaffId(Controller c)
	{
		String sessionID=c.getSession().getId();
		HashMap<String, Object > sessionMap=c.getSessionAttr(sessionID);
		return sessionMap.get("staffId").toString();
	}
	
	
	/**
	 * 获得orgId
	 * @param c
	 * @return
	 */
	public static String getSessionHolderId(Controller c)
	{
		String sessionID=c.getSession().getId();
		HashMap<String, Object > sessionMap=c.getSessionAttr(sessionID);
		if (sessionMap!=null) {
			return sessionMap.get("orgId").toString();
		}else {
			return "";
		}
		
	}
	/**
	 * 获得userType(即校方帐户类型,1为主账号)
	 * @param c
	 * @return
	 */
	public static String getSessionStaffType(Controller c)
	{
		String sessionID=c.getSession().getId();
		HashMap<String, Object > sessionMap=c.getSessionAttr(sessionID);
		return sessionMap.get("userType").toString();
	}
	
	/**
	 * 获得校方tcuId(即实际操作者id)
	 * @param c
	 * @return
	 */
	public static String getSessionTcuId(Controller c)
	{
		String sessionID=c.getSession().getId();
		HashMap<String, Object > sessionMap=c.getSessionAttr(sessionID);
		if (sessionMap!=null) {
			return sessionMap.get("tcuId").toString();
		}else {
			return "";
		}
		
	}
	/**
	 * 获得当前用户的管理者角色
	 * @param c
	 * @return
	 */
	public static String getSessionIsManager(Controller c)
	{
		String sessionID=c.getSession().getId();
		HashMap<String, Object > sessionMap=c.getSessionAttr(sessionID);
		return sessionMap.get("isManager").toString();
	}
	/**
	 * 获得未来的日期与星期表
	 * @param dayNum
	 * @return
	 */
	public static List<HashMap<String, String>> getNextDays(int dayNum)
	{
		List<HashMap<String, String>> rl=new ArrayList<HashMap<String,String>>(); 
		Format f = new SimpleDateFormat("yyyy-MM-dd"); 
		Date today = new Date();    
        Calendar c = Calendar.getInstance();  
        int dayForWeek=0;
		for (int i = 0; i < dayNum; i++) { 
			HashMap<String, String> map=new HashMap<String, String>();
	        c.setTime(today);  
	        c.add(Calendar.DAY_OF_MONTH, i);// 今天+i天  
	        Date nextDay = c.getTime(); 
	        map.put("date", f.format(nextDay)) ;
	        if(c.get(Calendar.DAY_OF_WEEK) == 1){  
	        	  dayForWeek = 7;  
        	 }else{  
        	  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
        	 }  
	        map.put("dayOfWeek",dayForWeek+"") ;
	        rl.add(map);
		}
		
		return rl;
		
	}
	/**
	 *  获得星期表(文本)
	 * @param datetime
	 * @return
	 */
	public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	/**
	 * 获得星期几（数字形式，0~6）0为周1一，6为周天
	 * @param datetime
	 * @return
	 */
	public static int dateToWeekNum(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        int[] weekDays = { 6,0,1,2,3,4,5 };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	/**
	 * 获得输入日期到达下个周的结束需要的天数
	 * @param datetime
	 * @return
	 */
	public static int getNumOfNextEndWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        int[] weekDays = { 7, 13, 12, 11, 10, 9, 8 };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	/**
	 * 改变当前天数
	 * @param baseDate
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	public static String dateChange(String baseDate,int days)  
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
		  Date dt = null;
		try {
			dt = sdf.parse(baseDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Calendar rightNow = Calendar.getInstance();
		  rightNow.setTime(dt);
		//  rightNow.add(Calendar.YEAR,-1);//日期减1年
		//  rightNow.add(Calendar.MONTH,3);//日期加3个月
		  rightNow.add(Calendar.DATE,days);//日期加
		  Date dt1=rightNow.getTime();
		  String reStr = sdf.format(dt1);
		  
		  return reStr;
	}
 

}
