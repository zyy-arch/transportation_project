package com.tsAdmin.common.intercept;

import java.util.HashMap;
import java.util.List;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.activerecord.Record;
import com.tsAdmin.common.CommonService;
/**
 * 登录检查拦截器
 * @author lzh
 *
 */
public class LoginIntercept implements Interceptor{
	public void intercept( Invocation ai) {
			//System.out.println("action注入之前,登录检查");
			//检查登录
			String sessionID=ai.getController().getSession().getId();
			HashMap<String, Object > sessionMap=ai.getController().getSessionAttr(sessionID);
			if(sessionMap==null)
				ai.getController().redirect("/org/login");
			else {
 
				 
					ai.getController().setAttr("orgInfo",sessionMap );
			 
 
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
