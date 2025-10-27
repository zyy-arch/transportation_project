package com.tsAdmin.common.task;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.tsAdmin.common.tools;
import com.tsAdmin.log.LogService;

public class MyTask implements ITask {
   public void run() {
      // 这里放被执行的调试任务代码
	   System.out.println("expclass set task");
	   
	   //LogService.insertLogSys("定时任务开始",tools.getDataTimeString());
	 
		 
   }
   
   public void stop() {
     // 这里的代码会在 task 被关闭前调用
   }
}