package com.tsAdmin.common.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;

public class setTime implements Job{
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
        // TODO Auto-generated method stub
		System.out.println("task start");
//		int timeCompressRate = Integer.parseInt(Db.findFirst("select * from sys_env where id=?", 1).getStr("values")); 
//		int startTime = (int) (System.currentTimeMillis()*0.001);
//		int currentTime = (int) (startTime + ((timeCompressRate-1) * 60));
//		Db.update("update sys_time set startTime=?, currentTime=?", startTime, currentTime);
//		System.out.println("task end");
	}
}
