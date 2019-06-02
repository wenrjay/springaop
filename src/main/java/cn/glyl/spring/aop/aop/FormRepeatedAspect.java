package cn.glyl.spring.aop.aop;

import cn.glyl.spring.aop.redis.RedisService;
import cn.glyl.spring.aop.util.MD5;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jay
 */
@Aspect
@Component
public class FormRepeatedAspect {

    @Autowired
    private RedisService redisService;

    public static final String VALUE = "v";

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(cn.glyl.spring.aop.annotation.FormRepeatedAnnotation)")
    public void FormRepeatedAspect() {

    }

    @Before("execution(* cn.glyl.spring.aop.web.*.*(..))")
    public void repeated(JoinPoint point) {
        Object[] args = point.getArgs();
        if (args == null) {
            return;
        }
        HttpServletRequest request = getHttpServletRequest(args);
        if (request == null) {
            throw new RuntimeException(" request is null ");
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        List<String> list = new ArrayList<>(parameterMap.keySet());
        Collections.sort(list);
        String parmaStr = list.stream()
                .map(o1 -> o1 + "=" + Arrays.toString(parameterMap.get(o1)))
                .collect(Collectors.joining("&"));
        String uri = request.getRequestURI();
        String sign = MD5.MD5Encode(uri + parmaStr + uri);
        Boolean res = redisService.setnx(sign, VALUE, 3L);
        if (!res) {
            throw new RuntimeException("请误重复提交");
        }
    }

    private HttpServletRequest getHttpServletRequest(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        return null;
    }


}
