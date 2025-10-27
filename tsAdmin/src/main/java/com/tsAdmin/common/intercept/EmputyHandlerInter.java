package com.tsAdmin.common.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.core.Action;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import com.jfinal.upload.MultipartRequest;
import com.tsAdmin.common.tools;

/**
 * Created by Pencilso on 2017/5/4.
 */
public class EmputyHandlerInter extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        String urlPara[] = {null};
        Action action = JFinal.me().getAction(target, urlPara);
        try {
        	EmptyInterface annotation = action.getMethod().getAnnotation(EmptyInterface.class);
        	 if (annotation != null) {
                 noEmpty(annotation, target, request, response, isHandled);
             } 
        	 else  
        		 next.handle(target, request, response, isHandled);
		} catch (Exception e) {
			// TODO: handle exception
		
		}
        
        
       
    }

    public void noEmpty(EmptyInterface annotation, String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        String header = request.getHeader("Content-Type"); //取出head头
        if (header != null && header.indexOf("multipart/form-data") != -1) { //判断是否是form-data 否则有可能会报错  之前线上出现过一次log信息
            request = new MultipartRequest(request);
            ((MultipartRequest) request).getFiles();
        }
        String[] value = annotation.value();
        for (String v : value) {
            String parameter = request.getParameter(v);
            if (parameter == null || parameter.trim().length() == 0) {
                tools.outEmpty(response, request, v);
                isHandled[0] = true;
                break;
            }
        }
        if (!isHandled[0])
            next.handle(target, request, response, isHandled);
    }
}
 