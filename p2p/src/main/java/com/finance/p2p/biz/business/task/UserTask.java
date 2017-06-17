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
 * 用户相关的计划处理
 * 
 * @author Administrator
 *
 */
@Component
@Lazy(false)
@EnableScheduling
public class UserTask extends LogUtil {

	private static String jobSwitch = Global.getJobSwitch();

	@Autowired
	private TaskService taskService;

	/**
	 * 用户锁定批处理
	 */
	@Scheduled(cron = "0 3/10 8-20 * * ?")
	public void userStateLockTask() {
		if (jobSwitch.equals("0")) {
			return;
		}
		log.info("开始执行用户锁定批处理");
		taskService.userStateLockTask();
	}

	/**
	 * 自动确认用户打款24小时内
	 */
	@Scheduled(cron = "0 5/5 1-6 * * ?")
	public void confirmPayTask() {
		if (jobSwitch.equals("0")) {
			return;
		}
		log.info("自动确认用户打款24小时内");
		taskService.confirmPayTask();
	}

	/**
	 * 计算用户利息，每天到达服务器凌晨开始处理
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void userInterestTask() {
		if (jobSwitch.equals("0")) {
			return;
		}
		taskService.userInterestTask();
	}
	
	/**
	 * 用户级别任务处理，每天凌晨0:30跑一次任务
	 */
	@Scheduled(cron = "0 30 0 * * ?")
	public void userLevelTask() {
		if (jobSwitch.equals("0")) {
			return;
		}
		taskService.userLevelTask();
	}
}
