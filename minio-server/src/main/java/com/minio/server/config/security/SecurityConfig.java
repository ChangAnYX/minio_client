package com.minio.server.config.security;

import com.minio.server.pojo.Admin;
import com.minio.server.service.IAdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * SpringSecurity配置类
 *
 * @author bin
 * @since 1.0.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private IAdminService adminService;
	@Resource
	private MyAccessDeniedHandler myAccessDeniedHandler;
	@Resource
	private MyAuthenticationEntryPoint myAuthenticationEntryPoint;


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/websocket/**",
				"/**.html",
				"/api/login/**",
				"/api/logout/**",
				"/api/sendEmail",
				"/api/register",
				"/api/captcha",
				"/css/**",
				"favicon.ico",
				"/doc.html",
				"/webjars/**",
				"/swagger-resources/**",
				"/v2/api-docs/**",
				"/api/object/**"

		);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//使用JWT，不需要CSRF
		http.csrf().disable()
				//禁用session
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				//放行登录及登出
				// .antMatchers("/login/**", "/logout/**").permitAll()
				.anyRequest().authenticated()
				.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
					@Override
					public <O extends FilterSecurityInterceptor> O postProcess(O object) {
						return object;
					}
				})
				.and()
				//禁用缓存
				.headers()
				.cacheControl();
		//JWT登录拦截器
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		//自定义异常返回结果
		http.exceptionHandling()
				.accessDeniedHandler(myAccessDeniedHandler)
				.authenticationEntryPoint(myAuthenticationEntryPoint);

	}

	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			Admin admin = adminService.getAdminByUserName(username);
			if (null == admin) {
				throw new UsernameNotFoundException("用户名或密码不正确");
			}
			return admin;
		};
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

}
