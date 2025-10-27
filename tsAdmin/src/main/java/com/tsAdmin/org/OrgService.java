package com.tsAdmin.org;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.tsAdmin.common.CommonService;
import com.tsAdmin.common.tools;
import com.tsAdmin.log.LogService;

public class OrgService {
	/**
	 * 增加一个组织
	 * @param orgName
	 * @param orgType
	 * @param orgOrder
	 * @param city_id
	 * @param loginName
	 * @param orgPassword
	 * @param state
	 * @param addTime
	 * @return
	 */
	
	public static int insertOrg(String orgName,String orgType,String orgOrder,String city_id,String loginName,String orgPassword,String state,String addTime)
	{
	Record record=new Record();
	record.set("orgName",orgName).set("orgType",orgType).set("orgOrder",orgOrder).set("city_id",city_id).set("loginName",loginName).set("orgPassword",orgPassword).set("state",state).set("addTime",addTime);
	 Db.save("t_org", record);
	 return Integer.parseInt(record.get("id").toString());

	}
	/**
	 * 所有城市
	 * @return
	 */
	public static List<Record> listAllcities() {
		List<Record> records=Db.find("SELECT * from mb_city  "); 
		return records;  
	}
	
	/**
	 * 所有城市
	 * @return
	 */
	public static List<Record> listAllOrgType() {
		List<Record> records=Db.find("SELECT * from mb_org_type "); 
		return records;  
	}
	/**
	 * 获得嘉宾列表
	 * @param campusId
	 * @param type
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	
	public static Page<Record> ListOrg(String orgId,String orgName, String orgType,	int pageNumber,int pageSize)
	{
		  
		String where=" where tor.orgType>0 ";//基本条件  
		String conStr=" and ";
		if (!orgName.equals("")) {
			where=where+conStr+" tor.orgName like '%"+orgName+"%' ";
			conStr=" and ";
		}
		if (!orgType.equals("0")) {
			where=where+conStr+" tor.id="+orgId+" ";  //只看自己的
			conStr=" and ";
		}
		
		String orderBy=" ORDER BY  tor.id DESC ";  
		String totalRowSql="SELECT count(tor.id) from t_org tor "+where;
		
		String findSql="SELECT tor.*,mc.cityName,mot.valueName as orgTypeName "
				+ ",(select tlo.addTime from t_log_org tlo where tlo.user_id=tor.id order by tor.id desc limit 1 ) as lastLogTime "
				+ " from t_org tor "
				+ "LEFT JOIN mb_city mc on mc.id=tor.city_id "
				+ "left join mb_org_type mot on mot.id=tor.orgType "
				+where+orderBy; 
		return Db.paginateByFullSql( pageNumber ,  pageSize , totalRowSql, findSql);
	}
	/**
	 * 增加一条用户日志
	 * @param user_id
	 * @param actionType 1为登录
	 * @param addTime
	 * @return
	 */
	public static int insertLogUser(String user_id,String actionType,String addTime)
	{
	Record record=new Record();
	record.set("user_id",user_id).set("actionType",actionType).set("addTime",addTime);
	 Db.save("t_log_user", record);
	 return Integer.parseInt(record.get("id").toString());

	}
	/**
	 * 获得session数据(普通用户)
	 * @param  
	 * @return
	 */
	public static HashMap<String, Object> getSessionDataUser(Record user)
	{
		//String staffType="0";
		//campus_id=Integer.parseInt(campus_id)+"";//校区id
		HashMap<String, Object > sessionMap=new HashMap<String,Object>();
	 
		 
			sessionMap.put("orgName", user.getStr("orgName"));
			sessionMap.put("orgId", user.getStr("id")); 
			sessionMap.put("orgType", user.getInt("orgType")); 
		return sessionMap;
	}
	/**
	 * 检查单位账号的密码
	 * @param table
	 * @param phone
	 * @return
	 */
	public static int isOrgPassExist(String orgId,String pass) {
		 
		Record record=Db.findFirst("select id from t_org where id=? and orgPassword=? ",orgId,pass);
		if (record!=null) {
			return record.getInt("id");
		} 
		return 0;
	}
	/**
	 * 更新单位密码
	 * @param orgPassword
	 * @param id
	 * @return
	 */
	public static int updateOrgPassword(String orgPassword,String id)
	{
	String sql="update t_org set orgPassword=? where id=?";
	return Db.update(sql,orgPassword,id);
	}
//	public static int resetPasswordToWx(String tcuId,int first){
//		int r=1;
//		
//		
//		String newPassword=tools.getRandomString(6);
//		String sha1=DigestUtils.sha1Hex(newPassword);
//		
//		UserService.updatePasswordBy(tcuId, sha1);
//		
//		 
//		Record tcu=UserService.getTcuBy(tcuId);
//		
//		//微信发送消息
//		
//		 JSONObject json = new JSONObject();
//		 JSONObject jsonFirst = new JSONObject();
//         jsonFirst.put("value", tcu.getStr("schoolShortName")+"--"+tcu.getStr("campusName"));
//         jsonFirst.put("color", "#173177");
//         json.put("first", jsonFirst);
//         
//         JSONObject jsonOrderMoneySum = new JSONObject();
//         jsonOrderMoneySum.put("value", tcu.getStr("username"));
//         jsonOrderMoneySum.put("color", "#173177");
//         json.put("keyword1", jsonOrderMoneySum);
//         JSONObject jsonOrderProductName = new JSONObject();
//         jsonOrderProductName.put("value", newPassword);
//         jsonOrderProductName.put("color", "#173177");
//         json.put("keyword2", jsonOrderProductName);
//         JSONObject jsonRemark = new JSONObject();
//         if (first==1) {
//        	 jsonRemark.put("value", "帐号关联已通过，请尽快登录修改密码！");
//		}else {
//			 jsonRemark.put("value", "请尽快登录修改！");
//		}
//         
//         jsonRemark.put("color", "#173177");
//         json.put("remark", jsonRemark);
//         
//         String openid=tcu.getStr("openid");
//         String templat_id="eo78WfBSF3Khdph50-zNacCAqDKcN_AWapyBgo-7ukw";
//         CommonService.sendWechatmsgToUser(openid, templat_id, "",  json);
//         
//         return r;
//		
//	}
//	/**
//	 * 检查tcu与campus所属关系
//	 * @param tcuId
//	 * @param campus_id
//	 * @return
//	 */
//	public static Record isTcuOfCampus(String tcuId,String campus_id) {
//		
//		Record rel=Db.findFirst("select * from t_campus_user where id=? and campus_id=? and (vaild=1 or vaild=2)",tcuId,campus_id);
//		return rel;
//				
//		
//	}
//	
//	/**
//	 * 列出已关联的校区账号
//	 * @param campusId
//	 * @return
//	 */
//	public static List<Record> listMyLinkedUsers(String campusId)
//	{
//		
//		
//		List<Record>r=Db.find("SELECT * from t_campus_user tcu where campus_id=? and (vaild=1 or vaild=2) ",campusId);
//		for (int i = 0; i < r.size(); i++) {
//			if (r.get(i).getStr("linkDate")==null) {
//				r.get(i).set("linkDate", "");
//			}
//			if (r.get(i).getStr("openid")==null) {
//				r.get(i).set("openid", "");
//			}
//			if (r.get(i).getStr("nickName")==null) {
//				r.get(i).set("nickName", "");
//			}
//		}
//		
//		return r;
//	}
//	
//	
//	public static Record getUserWXInfo(Controller c){
//		Record r=new Record();
//		
//		String code =c.getPara("code");
//		//String maRedirect=c.getPara("");
//		String maRedirect=c.getPara("maRedirect");//跳转到页
//		if (maRedirect==null) {
//			maRedirect="";
//		}
//		if (code != null&&(!"authdeny".equals(code))) {
//					//获取openid和access_token的连接
//	        String getOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?"
//	        		+ "appid=wx6e324db28f6d41ec&secret=cafc5200b3d862ed94f033ad017a7c0c&code=CODE&grant_type=authorization_code";
//	        //替换返回的code
//	        String requestUrl = getOpenIdUrl.replace("CODE", code); 
//	        //向微信发送请求并获取response 
//	        String response = HttpKit.get(requestUrl);
//	        JSONObject jb=JSONObject.parseObject(response); 
//	        if(jb==null||jb.get("access_token")==null)
//	        {
//	        	return null;
//	        }
//	        String access_token = jb.get("access_token").toString();
//	        String openid = jb.get("openid").toString();
//	        //System.out.println("=======================用户access_token==============");
//	        r.set("access_token", jb.get("access_token").toString());
//	        r.set("openid", jb.get("openid").toString()); 
//	        
//	        
//	        //获取用户基本信息的连接
//	        String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
//	        String userInfoUrl = getUserInfo.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
////	        HttpGet httpGetUserInfo = new HttpGet(userInfoUrl);
//	        String userInfo = HttpKit.get(userInfoUrl);
////	        //微信那边采用的编码方式为ISO8859-1所以需要转化
//	        String json="";
////				try {
////					json = new String(userInfo.getBytes("ISO-8859-1"),"UTF-8");
////				} catch (UnsupportedEncodingException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//			json=userInfo;
//			r.set("jsondata", json);
//	        System.out.println("====================wx=get=userInfo==============================");
//	        JSONObject jsonObject1 = JSONObject.parseObject(json);
//	        String nickname = jsonObject1.get("nickname").toString();
//	        String city = jsonObject1.get("city").toString();
//	        String province = jsonObject1.get("province").toString();
//	        String country = jsonObject1.get("country").toString();
//	        String headimgurl = jsonObject1.get("headimgurl").toString();
//	        //性别  1 男  2 女  0 未知
//	        String sex = jsonObject1.get("sex").toString();
//	        
//	        r.set("nickname", nickname);
//	        r.set("city", city);
//	        r.set("province", province);
//	        r.set("country", country);
//	        r.set("headimgurl", headimgurl);
//	        r.set("sex", sex);
//	        r.set("maRedirect", maRedirect);
//
//        
//		}
//		
//		
//		
//		return r;
//	}
//	/**
//	 * 更新一个登录错误记录
//	 * @param errorCount
//	 * @param note
//	 * @param id 
//	 * @return
//	 */
//	public static int updateLoginError(String note,String id)
//	{
//	String sql="update t_log_login_error set errorCount=errorCount+1,note=? where id=?";
//	return Db.update(sql,note,id);
//	}
//	/**
//	 * 增加一条错误记录
//	 * @param ip
//	 * @param logDate
//	 * @param errorCount
//	 * @param note
//	 * @return
//	 */
//	public static int insertLoginError(String ip,String logDate,String errorCount,String note)
//	{
//	Record record=new Record();
//	record.set("ip",ip).set("logDate",logDate).set("errorCount",errorCount).set("note",note);
//	 Db.save("t_log_login_error", record);
//	 return Integer.parseInt(record.get("id").toString());
//
//	}
//	
//	public static String getPasswordByHolderPhone(String phone) {
//		String real_password="";
//		Record r=Db.findFirst("select password,`status` from school_holder where tel=?",phone);
//		if (r!=null&&r.get("password")!=null) {
//			real_password=r.get("password").toString();
//		}
//		if (r!=null&&r.get("status")!=null&&(!r.get("status").toString().equals("1"))) {//账号状态为禁用
//			real_password=r.get("status").toString();
//		}
//		return real_password;
//		 
//	}
//	/**
//	 * 获得校区账号登录密码
//	 * @param phone
//	 * @return
//	 */
//	public static String getPasswordByCampus(String phone) { 
//		Record r=Db.findFirst("select pass  from campus where id=?",phone);
//		if (r!=null&&r.get("pass")!=null) {
//			return  r.get("pass").toString();
//		}else {
//			return "";
//		}
//		  
//	}
//	/**
//	 * 获得校区账号登录密码（新）根据登录名来
//	 * @param userName
//	 * @return
//	 */
//	public static Record getPasswordByCampus_New(String userName) { 
//		Record r=Db.findFirst("select *  from t_campus_user where username=? and vaild=1",userName);
//		if (r!=null) {
//			return  r;
//		}else {
//			return null;
//		}
//		  
//	}
//	/**
//	 * 
//	 * 根据工号获得密码
//	 * @return
//	 */
//	public static String getPasswordBystaffNumber(String staffNumber) {
//		String real_password="";
//		Record r=Db.findFirst("select password,`status` from staff where staffNumber=?",staffNumber);
//		if (r!=null&&r.get("password")!=null) {
//			real_password=r.get("password").toString();
//		}
//		if (r!=null&&r.get("status")!=null&&(!r.get("status").toString().equals("1"))) {//账号状态为禁用
//			real_password=r.get("status").toString();
//		}
//		return real_password;
//		 
//	}
//	
	
//	
//	/**
//	 * 获得session数据
//	 * @param staffNumber
//	 * @return
//	 */
//	public static HashMap<String, Object> getSessionData(String tcuId,int sudo)
//	{
//		//String staffType="0";
//		//campus_id=Integer.parseInt(campus_id)+"";//校区id
//		HashMap<String, Object > sessionMap=new HashMap<String,Object>();
//		
//		Record r=Db.findFirst("SELECT c.*,tcu.user_type_id,tcu.id as tcuId, s.schoolFullName,s.schoolShortName "
//				+ "from t_campus_user tcu "
//				+ "left join campus c on tcu.campus_id=c.id "
//				+ "LEFT JOIN school s on s.id=c.school_id "
//				//+ "left join t_campus_user tcu on tcu.campus_id=c.id "
//				+" WHERE tcu.id=? ",tcuId);
//		if (r!=null) {
//			sessionMap.put("userId", r.getStr("id"));
//			sessionMap.put("tel", r.getStr("managerTel"));
//			sessionMap.put("realName", r.getStr("manager").toString());
//			sessionMap.put("sfName", r.getStr("schoolShortName").toString());
//			sessionMap.put("ssName", r.getStr("campusName").toString());
//			sessionMap.put("userType", r.getStr("user_type_id"));
//			sessionMap.put("tcuId", r.getStr("tcuId"));
//			
//			if (sudo==1) {
//				sessionMap.put("userType",0);//如果超级用户，用户类型改为0
//			}
//			sessionMap.put("status", 1); 
//		}
// 
//		
//		return sessionMap;
//	}
//	/**
//	 * 获得密码(by tcu)
//	 * @param staffId
//	 * @return
//	 */
//	public static String getPasswordBy(String staffId)
//	{
//		String rs="";
//		//Record r=Db.findFirst("select password from school_holder where id=?",staffId);
//		Record r=Db.findFirst("select password from t_campus_user where id=?",staffId);
//		if (r!=null) {
//			rs=r.getStr("password");
//		}
//		return rs;
//	}
//	/**
//	 * 获得员工数据
//	 * @param staffId
//	 * @return
//	 */
//	public static Record getStaffInfo(String staffId)
//	{ 
//		Record r=Db.findFirst("select * from staff where id=?",staffId);
//		 
//		return r;
//	}
//	
//	/**
//	 * 更新密码(用tcu id)
//	 * @param staffId
//	 * @param newPassword
//	 * @return
//	 */
//	public static int updatePasswordBy(String staffId,String newPassword)
//	{
//		int i=0;
//		i=Db.update("update t_campus_user set password=? where id=?",newPassword,staffId);
//		return i;
//	}
//	/**
//	 * 插入权限
//	 * @param staffId
//	 * @param newPassword
//	 * @return
//	 */
//	public static boolean insertPriority(String staffTypeId,String mb_function_id)
//	{
//		 
//		Record record=new Record();
//		record.set("mb_staff_type_id", staffTypeId).set("mb_function_id", mb_function_id);
//		
//		
//		return Db.save("rel_priority", record);
//	}
//	/**
//	 * 获得所有某类员工
//	 * @return
//	 */
//	public static List<Record> getStaffsBy(String typeId) {
//		
//		 
//		List<Record> records=Db.find("SELECT id, realName from staff where `status`=1 and mb_staff_type_id=?",typeId);
//		 
//		return records;  
//	}
//	
//	/**
//	 * 获得所有帐号表信息
//	 * @return
//	 */
//	public static Record getTcuBy(String tcuId) {
//		
//		 
//		Record records=Db.findFirst("SELECT tcu.*,c.campusName,s.schoolShortName from t_campus_user tcu"
//				+ " left join campus c on c.id=tcu.campus_id "
//				+ "left join school s on s.id=c.school_id "
//				+ "where tcu.id=?",tcuId);
//		 
//		return records;  
//	}
//	/**
//	 * 插入学校帐号
//	 * @param campus_id
//	 * @param username
//	 * @param password
//	 * @param openid
//	 * @param linkDate
//	 * @param user_type_id
//	 * @param nickName
//	 * @param phone
//	 * @param vaild
//	 * @param updateDate
//	 * @param addDate
//	 * @return
//	 */
//	public static int insertTcu(String campus_id,String username,String password,String openid,String linkDate,String user_type_id,String nickName,String phone,String vaild,String updateDate,String addDate)
//	{
//	Record record=new Record();
//	record.set("campus_id",campus_id).set("username",username).set("password",password).set("openid",openid).set("linkDate",linkDate).set("user_type_id",user_type_id).set("nickName",nickName).set("phone",phone).set("vaild",vaild).set("updateDate",updateDate).set("addDate",addDate);
//	 Db.save("t_campus_user", record);
//	 return Integer.parseInt(record.get("id").toString());
//
//	}
}
