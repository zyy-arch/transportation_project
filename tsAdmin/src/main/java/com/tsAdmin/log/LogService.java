package com.tsAdmin.log;

import java.util.HashMap;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.tsAdmin.common.tools;

public class LogService {
	/**
	 * 增加组织日志
	 * @param user_id
	 * @param objId
	 * @param tableName
	 * @param action
	 * @param addTime
	 * @return
	 */
	public static int insertOrgLog(String user_id,String objId,String tableName,String action,String addTime)
	{
	Record record=new Record();
	record.set("user_id",user_id).set("objId",objId).set("tableName",tableName).set("action",action).set("addTime",addTime);
	 Db.save("t_log_org", record);
	 return Integer.parseInt(record.get("id").toString());

	}
	
	
}
