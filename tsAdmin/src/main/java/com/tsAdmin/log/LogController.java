package com.tsAdmin.log;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller; 
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.tsAdmin.common.tools;

/**
 *  
 * log功能
 * @李贞昊
 * 
 * IndexController
 */
public class LogController extends Controller {
	public void index() {
		
		 
		
		renderText("log is running");
	}
	
	
	 
}



