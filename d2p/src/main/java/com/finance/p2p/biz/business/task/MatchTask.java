package com.finance.p2p.biz.business.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.finance.p2p.biz.business.service.TaskService;
import com.finance.p2p.biz.sys.utils.Global;
import com.framework.utils.LogUtil;

/**
 * 预付款与余额匹配批处理
 * 
 * @author Administrator
 *
 */
@Component
@Lazy(false)
@EnableScheduling
public class MatchTask extends LogUtil {

	private static String jobSwitch = Global.getJobSwitch();

	@Autowired
	private TaskService taskService;

	/**
	 * 修改为对用户100%匹配
	 */
	@Scheduled(cron = "0/20 * 9,10,11 * * ?")
	public void userSurplusTask() {
		if (jobSwitch.equals("0")) {
			return;
		}
		log.info("开始执行全款匹配批处理");
		taskService.userSurplusTask();
	}

	/**
	 * 对内置用户进行全额匹配
	 */
	@Scheduled(cron = "10/20 * 9,10,11 * * ?")
	public void userInnerMatchTask() {
		if (jobSwitch.equals("0")) {
			return;
		}
		log.info("开始执行对内置用户进行全额匹配批处理");
		taskService.userInnerMatchTask();
	}
}
