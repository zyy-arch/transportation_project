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
 * 主帐号检查器
 * @author lzh
 *
 */
public class MainUserIntercept implements Interceptor{
	public void intercept( Invocation ai) {
			//System.out.println("action注入之前,登录检查");
			//检查登录
			//String sessionID=ai.getController().getSession().getId();
			//HashMap<String, Object > sessionMap=ai.getController().getSessionAttr(sessionID);
			String userType=tools.getSessionStaffType(ai.getController());
			if(!userType.equals("1"))
			{
				JSONObject reData=new JSONObject(); 
				 
					reData.put("code", "604");
					reData.put("msg", "您的帐号类别无此权限");
					ai.getController().renderJson(reData);return;
				 
				//ai.getController().redirect("/user/login");
			}
			else {
//				ai.getController().setAttr("realName", sessionMap.get("realName").toString());
//				ai.getController().setAttr("sfName", sessionMap.get("sfName").toString());
//				ai.getController().setAttr("ssName", sessionMap.get("ssName").toString());
//				ai.getController().setAttr("status", sessionMap.get("status").toString().equals("0")? "禁用":"启用");
//				ai.getController().setAttr("ele", new HashMap<String,String>());//放入空菜单权限
				//检查功能权限
// 				if (!CommonService.priorityCheck((HashMap<String, String>)sessionMap.get("fn_priority"), ai.getActionKey())) {
// 					ai.getController().renderText("priority err");
// 				}else {
//					//放入菜单权限
//					ai.getController().setAttr("ele", sessionMap.get("ele_priority"));
//					ai.invoke();
//				 }
				ai.invoke();
				
			}
			
			
			/*
	        System.out.println("actionKey:" + ai.getActionKey() + 
	        "--controllerKey" + ai.getControllerKey() +
	        "--methodname:" + ai.getMethodName() + 
	        "--viewPath:" + ai.getViewPath());*/
	        //System.out.println("action注入之后");
	 }

}
