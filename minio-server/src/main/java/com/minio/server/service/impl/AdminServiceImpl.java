package com.minio.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.minio.server.config.security.JwtTokenUtil;
import com.minio.server.pojo.Admin;
import com.minio.server.mapper.AdminMapper;
import com.minio.server.pojo.RespBean;
import com.minio.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author bin
 * @since 2022-02-08
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 登陆后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
            //登陆
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (null== userDetails||!passwordEncoder.matches(password,userDetails.getPassword())){
                return RespBean.error("用户名或密码不正确");
            }
            if (!userDetails.isEnabled()){
                return RespBean.error("账户被禁用，请联系管理员");
            }
            //验证验证码
            String captcha = (String) request.getSession().getAttribute("captcha");
            if (StringUtils.isEmpty(code)|| !captcha.equals(code)){
            return RespBean.error("验证码填写错误");
            }
            //更新登陆用户对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //生成token
            String token = jwtTokenUtil.generatorToken(userDetails);
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("tokenHead",tokenHead);
            tokenMap.put("token",token);
            return RespBean.success("登录成功",tokenMap);
        }

    /**
     * 根据用户名获取用户对象
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username));
    }

    /**
     * 注册
     * @return
     */
    @Override
    public int register(Admin user) {
        int bol = adminMapper.insert(user);
        System.out.print(bol);
        if(bol ==1){
            Admin i = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",user.getUsername()));
            System.out.print(i.getId());
            return i.getId();
        }
        else {
            return 0;
        }
    }


}
