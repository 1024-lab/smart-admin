package net.lab1024.sa.common.config;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.annoation.NoNeedLogin;
import net.lab1024.sa.common.common.annoation.SaAuth;
import net.lab1024.sa.common.common.domain.RequestUrlVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * url配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-05-30 21:22:12
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Configuration
@Slf4j
public class UrlConfig {
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    /**
     * 获取每个方法的请求路径
     *
     * @return
     */
    @Bean
    public Map<Method, Set<String>> methodUrlMap() {
        Map<Method, Set<String>> methodUrlMap = Maps.newHashMap();
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();

            Set<String> urls = requestMappingInfo.getPatternsCondition().getPatterns();
            if (CollectionUtils.isEmpty(urls)) {
                continue;
            }
            HandlerMethod handlerMethod = entry.getValue();
            methodUrlMap.put(handlerMethod.getMethod(), urls);
        }
        return methodUrlMap;
    }

    /**
     * 需要进行url权限校验的方法
     *
     * @param methodUrlMap
     * @return
     */
    @Bean
    public List<RequestUrlVO> authUrl(Map<Method, Set<String>> methodUrlMap) {
        List<RequestUrlVO> authUrlList = Lists.newArrayList();
        for (Map.Entry<Method, Set<String>> entry : methodUrlMap.entrySet()) {
            Method method = entry.getKey();
            SaAuth saAuth = method.getAnnotation(SaAuth.class);
            if (null == saAuth) {
                continue;
            }
            List<RequestUrlVO> requestUrlList = this.buildRequestUrl(method, entry.getValue());
            authUrlList.addAll(requestUrlList);
        }
        log.info("需要权限校验的URL：{}", authUrlList.stream().map(e -> e.getUrl()).collect(Collectors.toList()));
        return authUrlList;
    }

    private List<RequestUrlVO> buildRequestUrl(Method method, Set<String> urlSet) {
        List<RequestUrlVO> requestUrlList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(urlSet)) {
            return requestUrlList;
        }
        //url对应的方法名称
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        List<String> list = StrUtil.split(className, ".");
        String controllerName = list.get(list.size() - 1);
        String name = controllerName + "." + methodName;
        //swagger 说明信息
        String methodComment = null;
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            methodComment = apiOperation.value();
        }
        for (String url : urlSet) {
            RequestUrlVO requestUrlVO = new RequestUrlVO();
            requestUrlVO.setUrl(url);
            requestUrlVO.setName(name);
            requestUrlVO.setComment(methodComment);
            requestUrlList.add(requestUrlVO);
        }
        return requestUrlList;
    }


    /**
     * 获取无需登录可以匿名访问的url信息
     *
     * @return
     */
    @Bean
    public List<String> noNeedLoginUrlList(Map<Method, Set<String>> methodUrlMap) {
        List<String> noNeedLoginUrlList = Lists.newArrayList();
        for (Map.Entry<Method, Set<String>> entry : methodUrlMap.entrySet()) {
            Method method = entry.getKey();
            NoNeedLogin noNeedLogin = method.getAnnotation(NoNeedLogin.class);
            if (null == noNeedLogin) {
                continue;
            }
            noNeedLoginUrlList.addAll(entry.getValue());
        }
        log.info("不需要登录的URL：{}", noNeedLoginUrlList);
        return noNeedLoginUrlList;
    }

    /**
     * 获取忽略的url信息
     *
     * @return
     */
    @Bean
    public List<String> ignoreUrlList() {
        List<String> ignoreUrlList = Lists.newArrayList();
        ignoreUrlList.add("/swagger-ui.html");
        ignoreUrlList.add("/swagger-resources/**");
        ignoreUrlList.add("/webjars/**");
        ignoreUrlList.add("/druid/**");
        ignoreUrlList.add("/*/api-docs");
        return ignoreUrlList;
    }

}