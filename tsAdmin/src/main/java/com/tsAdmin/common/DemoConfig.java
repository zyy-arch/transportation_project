package com.tsAdmin.common;

 
 
 
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal; 
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine; 
import com.tsAdmin.common.intercept.EmputyHandlerInter;
import com.tsAdmin.common.intercept.LoginIntercept;
import com.tsAdmin.common.task.Cron4jPlugin;
import com.tsAdmin.common.task.MyTask; 
import com.tsAdmin.common.xss.XssHandler; 
import com.tsAdmin.index.IndexController;
import com.tsAdmin.log.LogController; 
import com.tsAdmin.org.OrgController;
import com.tsAdmin.pannel.PannelController;  

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * API引导式配置
 */
public class DemoConfig extends JFinalConfig {
	
	/**
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 * 
	 * 使用本方法启动过第一次以后，会在开发工具的 debug、run config 中自动生成
	 * 一条启动配置，可对该自动生成的配置再添加额外的配置项，例如 VM argument 可配置为：
	 * -XX:PermSize=64M -XX:MaxPermSize=256M
	 */
	public static void main(String[] args) {
		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
		JFinal.start("src/main/webapp", 8080, "/", 5);
		
		/**
		 * 特别注意：IDEA 之下建议的启动方式，仅比 eclipse 之下少了最后一个参数
		 */
		// JFinal.start("src/main/webapp", 80, "/");
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("db.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setViewType(ViewType.FREE_MARKER);
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		
		me.add("/", IndexController.class, "/");	// 第三个参数为该Controller的视图存放路径 
		me.add("/org", OrgController.class, "/func/org");	// 组织管理
		me.add("/pannel", PannelController.class, "/func/pannel/");	// 面板管理 
	 
		me.add("/common", CommonController.class, "/func/common");	// 第三个参数为该Controller的视图存放路径 
	 
	}
	
	public void configEngine(Engine me) { 
		
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置 druid 数据库连接池插件
		 DruidPlugin druidPlugin;
		 String develop=PropKit.get("develop");  
		if (develop.trim().equals("1")) { 
			 druidPlugin = new DruidPlugin(PropKit.get("dev_jdbcUrl"), PropKit.get("dev_user"), PropKit.get("dev_password").trim());
		}else {
			 druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		}
		
		me.add(druidPlugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		// 所有映射在 MappingKit 中自动化搞定
		//	_MappingKit.mapping(arp);
		me.add(arp);
		
		//QuartzPlugin quartzPlugin =  new QuartzPlugin("job.properties");
        //me.add(quartzPlugin);
        
		//me.add(new Cron4jPlugin(PropKit.use("task.properties")));
        
		Cron4jPlugin cp = new Cron4jPlugin(); 
//		  cp.addTask("0 6 * * *", new MyTask());  
		  //me.add(cp); 
		
	}
	
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		//登录拦截器
		me.addGlobalActionInterceptor(new LoginIntercept());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		 me.add(new XssHandler(""));//xss过滤
		 me.add(new EmputyHandlerInter());//空值过滤
	}
}
