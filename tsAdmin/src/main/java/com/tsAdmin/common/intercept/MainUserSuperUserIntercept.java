package com.tsAdmin.common.intercept;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.activerecord.Record;
import com.tsAdmin.common.CommonService;
import com.tsAdmin.common.tools;
/**
 * 主帐号和超级帐号检查器
 * @author lzh
 *
 */
public class MainUserSuperUserIntercept implements Interceptor{
	public void intercept( Invocation ai) { 
			String userType=tools.getSessionStaffType(ai.getController());
			 
				JSONObject reData=new JSONObject(); 
				if(!(userType.equals("1")||userType.equals("0")))
				{
					reData.put("code", "604");
					reData.put("msg", "您的帐号类别无此权限");
					ai.getController().renderJson(reData);return;
				}
				//ai.getController().redirect("/user/login");
			 
			else { 
				ai.invoke();
				
			}
			
		 
	 }

}
