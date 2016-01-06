package com.tdu.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

import com.tdu.web.interceptor.ModelAndViewInterceptor;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfigurationAdapter.class)
public class FrameworkMvcAutoConfiguration {

	@Bean
	public WebMvcConfigurerAdapter frameworkWebMvcConfigurerAdapter(){
		return new WebMvcConfigurationAdapter();
	}
	
	private static class WebMvcConfigurationAdapter extends WebMvcConfigurerAdapter{
		@Override
		public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
			super.configureContentNegotiation(configurer);
		}

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			super.addResourceHandlers(registry);
			//registry.addResourceHandler("/download/**").addResourceLocations("file:" + downloadDir + "/");
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			super.addInterceptors(registry);
			registry.addInterceptor(new LocaleChangeInterceptor());
			registry.addInterceptor(new ThemeChangeInterceptor());
			registry.addInterceptor(new ModelAndViewInterceptor()).addPathPatterns("/**");
		}
	}
}
