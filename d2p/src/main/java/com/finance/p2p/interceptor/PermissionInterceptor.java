package com.finance.p2p.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.finance.p2p.biz.common.service.CacheService;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.entity.User;
import com.framework.exception.SessionException;

/**
 * 拦截器
 * 
 * @author Administrator
 *
 */
public class PermissionInterceptor implements HandlerInterceptor {

	private List<String> domainOrIps = new ArrayList<String>();

	private List<String> uncheckUrls = new ArrayList<String>();
	
	@Autowired
	private CacheService cacheService;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {

		boolean result = containsUrl(uncheckUrls, arg0.getServletPath());
		if (result) {
			return true;
		} else {

			User user = (User) arg0.getSession().getAttribute(USERKey.USER_SESSION);
			if (user == null) {
				// 说明用户没有登录，那么不可以继续，那么返回
				throw new SessionException();
			}
			String token = cacheService.get(USERKey.USER_TOKEN, user.getUserId().toString());
			if (!StringUtils.equals(token, user.getToken())) {
				throw new SessionException("您的账号在另外设备上登录,请重新登录");
			}
		}

		return true;
	}

	public boolean isInnerServer(List<String> domainOrIps, HttpServletRequest request) {
		String serverIP = request.getRemoteAddr();// IP:127.0.0.1
		String serverDomain = request.getServerName();//localhost

		boolean result = false;
		for (String domainOrIp : domainOrIps) {
			if (domainOrIp.equals(serverIP) || domainOrIp.equals(serverDomain)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean containsUrl(List<String> uncheckUrls, String requestUrl) {
		boolean result = false;
		for (String uncheckUrl : uncheckUrls) {

			if (uncheckUrl.equals(requestUrl)) {
				result = true;
				break;
			}

			if (uncheckUrl.endsWith("/*")) {
				String rpath = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
				String cpatch = uncheckUrl.replace("/*", "");
				if (rpath.equals(cpatch)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	public List<String> getDomainOrIps() {
		return domainOrIps;
	}

	public void setDomainOrIps(List<String> domainOrIps) {
		this.domainOrIps = domainOrIps;
	}

	public List<String> getUncheckUrls() {
		return uncheckUrls;
	}

	public void setUncheckUrls(List<String> uncheckUrls) {
		this.uncheckUrls = uncheckUrls;
	}

}
