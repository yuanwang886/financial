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
	 * 对用户进行10%预付款处理
	 */
	@Scheduled(cron = "5/10 * 5-23 * * ?")
	public void userSubsistTask() {
		if (jobSwitch.equals("0")) {
			return;
		}
		log.info("开始执行预付款批处理");
		taskService.userSubsistTask();
	}

	/**
	 * 对用户进行90%余款处理
	 */
	@Scheduled(cron = "0/10 * 5-23 * * ?")
	public void userSurplusTask() {
		if (jobSwitch.equals("0")) {
			return;
		}
		log.info("开始执行余款批处理");
		taskService.userSurplusTask();
	}
}
