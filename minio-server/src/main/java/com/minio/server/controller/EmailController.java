package com.minio.server.controller;

import com.minio.server.utils.MailServiceUtil;
import com.minio.server.pojo.RespBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @program: minio
 * @description：邮箱接口
 * @author: bin
 * @create: 2022-03-09 16:19
 **/

@Controller
@RequestMapping("/api")
public class EmailController {

    @Resource
    private MailServiceUtil mailServiceUtils;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发送验证码 redis存储验证码
     * @param toEmail 被发送的邮箱账号
     */
    @PostMapping("/sendEmail")
    @ResponseBody
    public RespBean sendEmail(String toEmail) {
        //生成6位随机数
        String i = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        try {
            //发送邮件
            mailServiceUtils.sendMail("********@qq.com", toEmail, "网盘验证码", i);
            //redis保存验证码
            redisTemplate.opsForValue().set(toEmail, i);
        } catch (Exception e) {
            return RespBean.error("发送邮件失败");
        }
        //三分过期
        redisTemplate.expire(toEmail, 60*3000, TimeUnit.MILLISECONDS);
        return RespBean.success("发送验证码成功，请在三分钟内填写");

    }

}
