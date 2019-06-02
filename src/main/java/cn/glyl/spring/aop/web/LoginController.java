package cn.glyl.spring.aop.web;

import cn.glyl.spring.aop.annotation.FormRepeatedAnnotation;
import cn.glyl.spring.aop.entity.UserEntity;
import cn.glyl.spring.aop.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jay
 */
@RestController
@RequestMapping(value = "/user")
public class LoginController {

    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @FormRepeatedAnnotation
    public UserEntity save(HttpServletRequest request, UserEntity user) {
        System.out.println(user.toString());
        return user;
    }
}
