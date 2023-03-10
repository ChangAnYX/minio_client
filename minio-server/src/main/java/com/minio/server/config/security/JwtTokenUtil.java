package com.minio.server.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author bin
 * @since 1.0.0
 */
@Component
public class JwtTokenUtil {


    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.expiration}")
    private Integer expiration;
    @Value("${jwt.secret}")
    private String secret;


    /**
     * 根据用户名生成token
     *
     * @param userDetails
     * @return
     */
    public String generatorToken(UserDetails userDetails) {
        Map<String, Object> map = new HashMap<>();
        map.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        map.put(CLAIM_KEY_CREATED, new Date());
        return generatorToken(map);
    }

    /**
     * 根据token获取用户名
     *
     * @return
     */
    public String getUserNameFormToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }

    /**
     * 验证token是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails){
        String username = getUserNameFormToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 验证token是否可以刷新
     * @param token
     * @return
     */
    public boolean isRefreshToken(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新Token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generatorToken(claims);
    }


    /**
     * 验证token是否过期
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date exp = claims.getExpiration();
        return exp.before(new Date());
    }


    /**
     * 根据token获取负载
     *
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * 根据负载生成Token
     *
     * @param map
     * @return
     */
    private String generatorToken(Map<String, Object> map) {
        return Jwts.builder()
                .setClaims(map)
                .setExpiration(generatorExpiration())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 失效时间
     *
     * @return
     */
    private Date generatorExpiration() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }


}
