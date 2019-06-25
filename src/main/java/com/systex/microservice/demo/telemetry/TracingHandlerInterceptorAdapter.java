package com.systex.microservice.demo.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class TracingHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 在 Log4j2 的 ThreadContext 中寫入資訊
        ThreadContext.put("tracing",toJson(getTraceHeaders(request)));

        return super.preHandle(request, response, handler);
    }
    private String toJson(Object obj) throws Exception{
        return new ObjectMapper().writeValueAsString(obj);
    }
    /**
     * 回傳 Tracing 相關的 headers
     * @return Map<String, String>
     */
    private Map<String, String> getTraceHeaders(HttpServletRequest request){
        Map<String, String> headers = Maps.newHashMap();

        // 不存在的 header 不能給值
        if ( request.getHeader("x-request-id") != null  )
            headers.put("x-request-id", request.getHeader("x-request-id"));
        if ( request.getHeader("x-b3-traceid") != null  )
            headers.put("x-b3-traceid", request.getHeader("x-b3-traceid"));
        if ( request.getHeader("x-b3-spanid") != null  )
            headers.put("x-b3-spanid", request.getHeader("x-b3-spanid"));
        if ( request.getHeader("x-b3-parentspanid") != null  )
            headers.put("x-b3-parentspanid", request.getHeader("x-b3-parentspanid"));
        if ( request.getHeader("x-b3-sampled") != null  )
            headers.put("x-b3-sampled", request.getHeader("x-b3-sampled"));
        if ( request.getHeader("x-b3-flags") != null  )
            headers.put("x-b3-flags", request.getHeader("x-b3-flags"));

        return headers;
    }
}
