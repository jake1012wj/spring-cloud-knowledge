package com.jeesite.modules.blog.web.config;

import com.jeesite.modules.blog.base.global.Constants;
import com.jeesite.modules.blog.utils.JsonUtils;
import com.jeesite.modules.blog.utils.StringUtils;
import com.jeesite.modules.blog.web.global.RedisConf;
import com.jeesite.modules.blog.web.global.SysConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 拦截器
 */
@Component

public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private static Logger log = LoggerFactory.getLogger(AuthenticationTokenFilter.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //得到请求头信息authorization信息
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null) {
            //从Redis中获取内容
            String userInfo = stringRedisTemplate.opsForValue().get(RedisConf.USER_TOKEN + Constants.SYMBOL_COLON + accessToken);
            if (!StringUtils.isEmpty(userInfo)) {
                Map<String, Object> map = JsonUtils.jsonToMap(userInfo);
                //把userUid存储到 request中
                request.setAttribute(SysConf.TOKEN, accessToken);
                request.setAttribute(SysConf.USER_UID, map.get(SysConf.UID));
                request.setAttribute(SysConf.USER_NAME, map.get(SysConf.NICK_NAME));
                log.info("解析出来的用户:{}", map.get(SysConf.NICK_NAME));
            }
        }
        chain.doFilter(request, response);
    }
}
		

