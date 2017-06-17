package com.framework.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.framework.SysConst;

/**
 * 
 * @author Administrator
 *
 */
public class ExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception arg3) {

		ModelAndView mv = new ModelAndView();

		MappingJackson2JsonView view = new MappingJackson2JsonView();
		Map<String, Object> attributes = new HashMap<>();
		// 系统业务异常
		if (arg3 instanceof BusinessException) {
			attributes.put("result", SysConst.YesOrNO.NO);
			attributes.put("desc", arg3.getMessage());
			view.setAttributesMap(attributes);
			mv.setView(view);
		} else if (arg3 instanceof SessionException) {
			// 其他异常
			mv.setViewName("redirect:/timeout");
		}
		return mv;
	}
}
