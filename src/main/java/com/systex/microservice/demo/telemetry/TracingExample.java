package com.systex.microservice.demo.telemetry;

import com.google.common.collect.Maps;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(path = "/tracing")
public class TracingExample {

    private @Autowired HttpServletRequest request;

    Logger log = LogManager.getLogger(TracingExample.class);

    public TracingExample(){
        log.info("test");
    }
    /**
     * 模擬購物車新增品項的 API 接口
     * @return
     * @throws Exception
     */
    @GetMapping(path="/list")
    public String item() throws Exception{

        HttpResponse<String> response = Unirest.get("http://telemetry-backend:8080/get/listQuote")
                .headers(getTraceHeaders()) // 將 tracing 相關 header 送給下一個  request
                .header("cache-control", "no-cache")
                .asString();

        HttpResponse<String> response2 = Unirest.get("http://telemetry-backend:8080/get/price/123")
                .headers(getTraceHeaders()) // 將 tracing 相關 header 送給下一個  request
                .header("cache-control", "no-cache")
                .asString();

        return response.getBody();
    }

    /**
     * 回傳 Tracing 相關的 headers
     * @return Map<String, String>
     */
    private Map<String, String> getTraceHeaders(){
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
