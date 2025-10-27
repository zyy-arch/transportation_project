package com.tsAdmin.common;


import java.util.List;











import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.tsAdmin.common.intercept.EmptyInterface; 

/**
 *  
 * 首页功能
 * @李贞昊
 * 
 * IndexController
 */
public class CommonController extends Controller {
	public void index() {
		renderText("common is ok");
	}
 
	 
 
	
	
}



