package com.microservice.vapro.gateway.service.impl;

import com.microservice.vapro.gateway.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: Mr.Lee
 * @create: 2019-01-02 01:16
 **/
@Service
public class PermissionServiceImpl implements PermissionService {
    private static final Logger logger= LoggerFactory.getLogger(PermissionServiceImpl.class);

    /**
     * 可以做URLs匹配，规则如下
     *
     * ？匹配一个字符
     * *匹配0个或多个字符
     * **匹配0个或多个目录
     * 用例如下
     * <p>https://www.cnblogs.com/zhangxiaoguang/p/5855113.html</p>
     */

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String requestUrl = null;
        try {
//            requestUrl = request.uri().toURL().toString();
            requestUrl=request.getRequestURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("requestUrl:{}",requestUrl);
        List<SimpleGrantedAuthority> grantedAuthorityList = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        boolean hasPermission = false;

        if (principal != null){
            if (CollectionUtils.isEmpty(grantedAuthorityList)){
                return hasPermission;
            }
            for (SimpleGrantedAuthority authority:grantedAuthorityList
            ) {
                if (antPathMatcher.match(authority.getAuthority(),requestUrl)){
                    hasPermission = true;
                    break;
                }
            }
        }

        return hasPermission;
    }
}
