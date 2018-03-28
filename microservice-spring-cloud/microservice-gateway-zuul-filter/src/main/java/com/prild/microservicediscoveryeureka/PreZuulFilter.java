package com.prild.microservicediscoveryeureka;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


public class PreZuulFilter extends ZuulFilter {
    private static final Logger log = LoggerFactory.getLogger(PreZuulFilter.class);

    @Override
    public String filterType() {        //过滤器的类型
        return "pre";
    }

    @Override
    public int filterOrder() {      //过滤器级别,执行顺序
        return 1;
    }

    @Override
    public boolean shouldFilter() {     //是否使用该过滤器
        return true;
    }

    @Override
    public Object run() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String host = request.getRemoteHost();
        log.info("请求的host：{}",host);
        return null;
    }
}
