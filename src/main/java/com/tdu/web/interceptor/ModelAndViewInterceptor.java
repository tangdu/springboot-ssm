package com.tdu.web.interceptor;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ModelAndViewInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null && handler!=null && handler instanceof HandlerMethod){
			HandlerMethod method=(HandlerMethod)handler;
			modelAndView.addObject("ControllerClass", method.getMethod().getDeclaringClass().getName());
			modelAndView.addObject("ControllerMethod", method.getMethod().getName());
			Annotation[] annos=method.getMethod().getDeclaredAnnotations();
			if(annos!=null && annos.length>0){
				Annotation anno=annos[0];
				if(anno instanceof RequestMapping){
					RequestMapping re=(RequestMapping)anno;
					modelAndView.addObject("ControllerMethodType", Arrays.asList(re.method()));
					modelAndView.addObject("ControllerUrl", Arrays.asList(re.value()));
				}
			}
		}
		super.postHandle(request, response, handler, modelAndView);
	}

}
