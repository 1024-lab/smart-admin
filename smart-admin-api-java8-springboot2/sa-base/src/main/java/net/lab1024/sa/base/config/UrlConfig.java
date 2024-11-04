package net.lab1024.sa.base.config;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.domain.RequestUrlVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
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
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Configuration
@Slf4j
public class UrlConfig {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 获取每个方法的请求路径
     */
    @Bean
    public Map<Method, Set<String>> methodUrlMap() {
        Map<Method, Set<String>> methodUrlMap = Maps.newHashMap();
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            if(requestMappingInfo.getPatternsCondition() == null){
               continue;
            }

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
            // 忽略权限
            SaIgnore ignore = method.getAnnotation(SaIgnore.class);
            if (null != ignore) {
                continue;
            }
            NoNeedLogin noNeedLogin = method.getAnnotation(NoNeedLogin.class);
            if (null != noNeedLogin) {
                continue;
            }
            Set<String> urlSet = entry.getValue();
            List<RequestUrlVO> requestUrlList = this.buildRequestUrl(method, urlSet);
            authUrlList.addAll(requestUrlList);
        }
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
        Operation apiOperation = method.getAnnotation(Operation.class);
        if (apiOperation != null) {
            methodComment = apiOperation.summary();
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


}