package com.minio.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录拦截器
 *
 * @author bin
 * @since 1.0.0
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.tokenHeader}")
	private String tokenHeader;
	@Value("${jwt.tokenHead}")
	private String tokenHead;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader(tokenHeader);
		//存在token
		if (!StringUtils.isEmpty(authHeader) && authHeader.startsWith(tokenHead)) {
			String authToken = authHeader.substring(tokenHead.length());
			//根据token获取用户名
			String username = jwtTokenUtil.getUserNameFormToken(authToken);
			//token中存在用户名但是SpringSecurity不存在(未登录)
			if (!StringUtils.isEmpty(username) && null == SecurityContextHolder.getContext().getAuthentication()) {
				//登录
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				//判断token是否有效
				if (jwtTokenUtil.validateToken(authToken, userDetails)) {
					//把用户对象设置到SpringSecurity全局上下文中
					UsernamePasswordAuthenticationToken authenticationToken =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
