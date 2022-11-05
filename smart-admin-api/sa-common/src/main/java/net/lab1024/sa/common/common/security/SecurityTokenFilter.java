package net.lab1024.sa.common.common.security;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.constant.RequestHeaderConst;
import net.lab1024.sa.common.common.domain.RequestUser;
import net.lab1024.sa.common.common.util.SmartRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 注意此处不能 加入@Component，否则对应ignoreUrl的相关请求 将会进入此Filter，并会覆盖CorsFilter
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/5/12 21:50
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
public class SecurityTokenFilter extends OncePerRequestFilter {

    private BiFunction<String,HttpServletRequest, UserDetails> userFunction;

    public SecurityTokenFilter(BiFunction<String,HttpServletRequest, UserDetails> userFunction) {
        this.userFunction = userFunction;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        //需要做token校验, 消息头的token优先于请求query参数的token
        String xHeaderToken = request.getHeader(RequestHeaderConst.TOKEN);
        String xRequestToken = request.getParameter(RequestHeaderConst.TOKEN);
        String xAccessToken = null != xHeaderToken ? xHeaderToken : xRequestToken;
        if (StringUtils.isBlank(xAccessToken)) {
            chain.doFilter(request, response);
            return;
        }
        //清理spring security
        SecurityContextHolder.clearContext();

        UserDetails loginUserDetail = userFunction.apply(xAccessToken,request);
        if (null != loginUserDetail) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDetail, null, loginUserDetail.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            SmartRequestUtil.setRequestUser((RequestUser) loginUserDetail);
        }
        // 若未给予spring security上下文用户授权 则会授权失败 进入AuthenticationEntryPointImpl
        chain.doFilter(request, response);
    }
}
