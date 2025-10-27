package com.tsAdmin.index;


import java.util.List;


import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
/**
 * 首页功能
 * @author lzh
 *
 */
public class IndexController extends Controller {
	
	public void index() {
		
		 
		
		renderText("system is running");
		redirect("/org/login");return;
	}
}



