package com.tsAdmin.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.tsAdmin.common.tools;

public class IndexService {
	/**
	 * 
	 * TSS虚拟时间
	 * @return
	 */
	public static int getTssTime() {
		
		String sqlString="SELECT * from sys_time";
		Record r=Db.findFirst(sqlString);
		if(r!=null) {
		 return r.getInt("currentTime");
		}
		return 0;
	}
}
