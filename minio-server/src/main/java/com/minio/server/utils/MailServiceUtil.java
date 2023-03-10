package com.minio.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;


/**
 * @program: minio
 * @description：邮箱工具类
 * @author: bin
 * @create: 2022-03-09 16:01
 **/

@Component
public class MailServiceUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JavaMailSender mailSender;

    /**
     * @param from 发送人
     * @param toEmail 接收人
     * @param subject 主题
     * @param content 内容
     */
    public void sendMail(String from,String toEmail, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message); logger.info("邮件成功发送!");
        } catch (MailException e) {
            logger.error("发送邮件错误！",e);
        }
    }

    /**
     * 邮箱验证码验证
     * @param toEmail 被发送的邮箱账号
     * @param EmailCode 输入的验证码判断
     * @return
     */
    public int yanZheng(String toEmail, String EmailCode) {
        //根据邮箱号取出验证码
        String co = (String) redisTemplate.opsForValue().get(toEmail);
        if(Objects.equals(co, EmailCode)){
            return 1;
        }
        if (co == null){
            return 2;
        }
        return 0;
    }

}
