package com.tsAdmin.org;


import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.json.Json;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
//import com.sun.prism.Image;
import com.tsAdmin.common.CommonService;
import com.tsAdmin.common.tools;
import com.tsAdmin.common.intercept.EmptyInterface;
import com.tsAdmin.common.intercept.LoginIntercept;
import com.tsAdmin.common.intercept.MainUserIntercept;
import com.tsAdmin.common.intercept.MainUserSuperUserIntercept;
import com.tsAdmin.common.sms.smsProvider;
import com.tsAdmin.log.LogService; 

/**
 *   
 * 
 * IndexController
 */

public class OrgController extends Controller {
	public void index() {
		renderText("staff is ok");
	}
	
	/**
	 * 
	 */
	public void doLogout() {
		String sessionID=getSession().getId();
		getSession().removeAttribute(sessionID);
		
		redirect("/org/login");
		return;
		
	}
	/**
	 * 
	 */
	@Clear
	public void doLoginOrg() {
		String para1=getPara("loginName");
		String para2=getPara("pass");
		System.out.println(para1+"\\"+para2);
		String mykeyString="af8d7eef6373429b855d68efaf3ed6af";
		int para3 = 5;
		
		//转换为URL编码用于传递中文
		try {
			para1=URLEncoder.encode(para1,"utf-8");
			para2=URLEncoder.encode(para2,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String FirstURL="https://restapi.amap.com/v3/place/text?"
				+ "keywords="+para2+"&city="+para1+"&offset=20&page=1&key="+mykeyString+"&extensions=all";

		String resText = HttpKit.get(FirstURL);
		
		for(int j = 1; j <= para3; j++) {
			String RequestURL="https://restapi.amap.com/v3/place/text?"
					+ "keywords="+para2+"&city="+para1+"&offset=20&page="+j+"&key="+mykeyString+"&extensions=all";
			resText=HttpKit.get(RequestURL);
			//解析并存储到数据库
			//1先解析
			JSONObject resOBJ=JSONObject.parseObject(resText);//把文本格式的json字符串转换为对象，便于取值
			JSONArray pois=resOBJ.getJSONArray("pois");
			
			//pois中，数据是array形式，所以用for循环
			for (int i = 0; i < pois.size(); i++) {
				String objString=pois.get(i).toString();//获得返回结果集中，第i个对象的string文本。
				JSONObject singleObj= JSONObject.parseObject(objString);//将对象由string重新转换为jsonObj
				String POI_name=singleObj.get("name").toString();//两种方式，一种是先get一个object，再转String，另一种是:getString
				String POI_location=singleObj.get("location").toString();
				String locationID=singleObj.getString("id");
				//由于经纬度是拼接在一个字符串中的，需要分割
				String POI_lat=POI_location.split(",")[0];//取分割后的第0个
				String POI_lon=POI_location.split(",")[1];//取分割后的第1个
				//系统调试输出
				System.out.println("ID:"+locationID+"地点是："+POI_name+",经度:"+POI_lat+"，纬度:"+POI_lon);
				//接下来根据locationID查询数据库中是否有存储或，若没有则使用DB.save()方法插入一行数据。
				
				Record test_poiRecord=new Record();
				test_poiRecord.set("poi_name", POI_name).set("poi_lat", POI_lat);
				
				test_poiRecord.set("poi_lon", POI_lon);
				test_poiRecord.set("poi_type", "1");
				test_poiRecord.set("poi_status", "1");
				test_poiRecord.set("create_time", tools.getDataTimeString());
				Db.save("poi", test_poiRecord);
				
//				String New_id="";
//				if (locationID.equals(New_id)) {//判断两个字符是否一致
//					
//				}
				
			}
		}
		
		//组装为json结构，用于前端显示
		JSONObject resJO=new JSONObject();
		resJO.put("resText", resText);
		resJO.put("code", 1);
		renderJson(resJO);
		//renderHtml("ok");
	}
	
	@Clear
	//@EmptyInterface({"loginName","pass"})
	public void viewmap() {
		//空接口（暂时没有数据传递）
	}
	
	@Clear
    public void viewMapPoint() {
		try {
	        String sql = "SELECT poi_name, poi_type, poi_lat, poi_lon, create_time FROM poi WHERE poi_status = 1";
	        List<Record> poiList = Db.find(sql);
	        
	        List<Map<String, Object>> resultList = new ArrayList<>();
            for (Record record : poiList) {
                Map<String, Object> poiMap = new HashMap<>();
                poiMap.put("poi_lat", record.getDouble("poi_lat"));
                poiMap.put("poi_lon", record.getDouble("poi_lon"));
                resultList.add(poiMap);
            }
            
	        JSONObject resJO=new JSONObject();
			resJO.put("data", resultList);
			resJO.put("code", 1);
			renderJson(resJO);
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 返回错误响应
	        renderJson(Ret.fail("code", -1).set("message", "数据查询失败").set("data", new ArrayList<>()));
	    }
    }
	
	
	/**
	 * 登录页面
	 */
	@Clear
	public void login() {
		//查询
		String thisTableString="poi";
		String sql="select * from "+thisTableString;//查询语句构造
		List<Record>resList=Db.find(sql);
		for (int i = 0; i < resList.size(); i++) {
			String thisName=resList.get(i).getStr("poi_name");
			System.out.println(thisName);
		}
		setAttr("resList", resList);//把数据传递到前端供获取
	}
	@Clear
	public void testLLM() {
		
		 
		
	}
	@Clear
    public void TrasnprotSIM() {
		
		 
		
	}
	/**
	 * 组织，修改密码
	 */
	public void orgPass() {
		 
		 
		
	}
	
	@EmptyInterface({"oldPass","newPass","newPass2"})
	public void doEditPass(){
		 

	}
	
	 
}



