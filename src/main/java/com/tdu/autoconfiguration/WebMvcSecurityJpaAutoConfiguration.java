package com.tdu.autoconfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

@Configuration
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@EnableWebSecurity
public class WebMvcSecurityJpaAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Bean
	@ConditionalOnMissingBean
	public WebSecurityConfigurer<WebSecurity> webMvcSecurityConfiguration() {
		return new WebMvcSecurityConfiguration();
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationManager webMvcSecurityAuthenticationManager(AuthenticationConfiguration auth)
			throws Exception {
		return auth.getAuthenticationManager();
	}

	private static class WebMvcSecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Autowired
		private Environment environment;

		@Autowired
		protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			Boolean eanbleCsrf = environment.getProperty("security.enable-csrf", Boolean.class, false);
			if (eanbleCsrf) {
				String csrfExcludes = environment.getProperty("security.csrf.excludes", String.class, null);
				if (StringUtils.hasText(csrfExcludes)) {
					CsrfProtectionMatcher csrfProtectionMatcher = new CsrfProtectionMatcher(csrfExcludes);
					http.csrf().requireCsrfProtectionMatcher(csrfProtectionMatcher);
				}
			} else {
				http.csrf().disable();
			}

			http.authorizeRequests().antMatchers(getPermitAllUrlAntMatchers()).permitAll().anyRequest().authenticated();

			http.formLogin()
				.loginPage("/login.html")
				.loginProcessingUrl("/login")
				.failureUrl("/login.html")
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/");
		}

		private class CsrfProtectionMatcher implements RequestMatcher {
			private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
			private List<AntPathRequestMatcher> excludeMatchers = new ArrayList<AntPathRequestMatcher>();

			public CsrfProtectionMatcher(String csrfExcludes) {
				if (StringUtils.hasText(csrfExcludes)) {
					String[] excludes = StringUtils
							.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(csrfExcludes));
					if (excludes != null && excludes.length > 0) {
						for (String exclude : excludes) {
							this.excludeMatchers.add(new AntPathRequestMatcher(exclude));
						}
					}
				}
			}

			public boolean matches(HttpServletRequest request) {
				boolean allowed = allowedMethods.matcher(request.getMethod()).matches();
				if (allowed) {
					return false;
				} else {
					for (AntPathRequestMatcher matcher : excludeMatchers) {
						if (matcher.matches(request)) {
							return false;
						}
					}
					return true;
				}
			}
		}

		private String[] getPermitAllUrlAntMatchers() {
			List<String> result = new ArrayList<String>();
			result.add("/webjars/**");
			result.add("/resources/**");
			return result.toArray(new String[] {});
		}
	}
}
