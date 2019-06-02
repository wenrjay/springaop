package cn.glyl.spring.aop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

@Configuration
public class StringConverter implements Converter<String, String> {
    @Override
    public String convert(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        return s.trim();
    }
}