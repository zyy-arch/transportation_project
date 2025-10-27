package com.tsAdmin.org;


import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
		String mykeyString="680174f12400afe6abc38c1c20d3cca1";
		
		
		//转换为URL编码用于传递中文
		try {
			para1=URLEncoder.encode(para1,"utf-8");
			para2=URLEncoder.encode(para2,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String RequestURL="https://restapi.amap.com/v3/place/text?"
				+ "keywords="+para2+"&city="+para1+"&offset=20&page=1&key="+mykeyString+"&extensions=all";
		String resText=HttpKit.get(RequestURL);
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
			String New_id="";
			if (locationID.equals(New_id)) {//判断两个字符是否一致
			}
			
			
			
			/*（已成功实现，用来将非重复数据插入数据库）
			// 去掉 locationID 相关逻辑，适配 poi_type 整数类型
			Record poiRecord = new Record();
			poiRecord.set("poi_name", POI_name);      // 地点名称（字符串类型，正常赋值）
			poiRecord.set("poi_lat", POI_lat);        // 纬度（根据表结构，若为字符串则直接用，若为数值需转成 Double）
			poiRecord.set("poi_lon", POI_lon);        // 经度（同纬度，注意类型匹配）
			// 关键修改：poi_type 用整数赋值（如 0 代表“默认类型”，1 代表“餐饮”等，根据你的业务定义）
			poiRecord.set("poi_type", 0);             // 整数 0 替代字符串“默认类型”，适配表的 INT 类型
			poiRecord.set("poi_status", 1);           // 状态也用整数（1 代表有效，符合整数类型）
			poiRecord.set("create_time", tools.getDataTimeString()); // 创建时间（字符串，正常赋值）

			// （可选）去重逻辑：通过“名称+经纬度”判断（避免重复插入）
			Record existingRecord = Db.findFirst(
			    "select * from e_poi where poi_name = ? and poi_lat = ? and poi_lon = ?",
			    POI_name, POI_lat, POI_lon
			);

			if (existingRecord == null) {
			    Db.save("e_poi", poiRecord); // 此时所有字段类型与表匹配，可正常插入
			    System.out.println("插入新POI记录：" + POI_name + "（类型：默认类型，对应值：0）");
			} else {
			    System.out.println("POI记录已存在，跳过插入：" + POI_name);
			}
			*/
			
			
			
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
	/**
	 * 登录页面
	 */
	@Clear
	public void login() {
		//插入数据
		Record e_poiRecord=new Record();
		e_poiRecord.set("poi_name", "九里堤加油站2").set("poi_lat", "104");
		
		e_poiRecord.set("poi_lon", "31");
		e_poiRecord.set("poi_type", "1");
		e_poiRecord.set("poi_status", "1");
		e_poiRecord.set("create_time", tools.getDataTimeString());//
		Db.save("e_poi", e_poiRecord);
		
		
		
		//查询
		String thisTableString="e_poi";
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



