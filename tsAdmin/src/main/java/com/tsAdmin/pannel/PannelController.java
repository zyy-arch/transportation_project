package com.tsAdmin.pannel;


import java.util.List;














import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mchange.v2.c3p0.impl.NewPooledConnection;
import com.tsAdmin.common.CommonService;
import com.tsAdmin.common.tools;
import com.tsAdmin.common.intercept.EmptyInterface; 

/**
 *  
 * 首页功能
 * @李贞昊
 * 
 * IndexController
 */
public class PannelController extends Controller {
	public void index() {
		renderText("pannel is ok");
	}
	 
	
	public void main() {
		 
	}
	
	public void projectGen() {
		 
	}
	 
}



