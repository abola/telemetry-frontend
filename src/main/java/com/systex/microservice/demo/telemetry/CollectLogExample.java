package com.systex.microservice.demo.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping(path = "/logging")
public class CollectLogExample {

    Logger log = LogManager.getLogger(CollectLogExample.class);

    /**
     * 模擬購物車新增品項的 API 接口
     * @param item
     * @return
     * @throws Exception
     */
    @GetMapping(path="/{item}/add")
    public String item(@PathVariable("item") String item) throws Exception{

        if (Strings.isNullOrEmpty(item) ) return "{\"status\":\"error\"}";
        // 為本筆交易建立一組 ID
        String uniqueId = UUID.randomUUID().toString();

        // 將 ID 與 物品的代號加入 Log
        Map<String, String> logMap = mapBuilder()
                .put("uniqueId",uniqueId)
                .put("item",item)
                .build();
        log.info(toJson(logMap));

        return "{\"status\":\"done\"}";
    }

    @GetMapping(path="/")
    public String entry() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("index.html");
        return readFromInputStream(inputStream);
    }
    private String readFromInputStream(InputStream inputStream)
            throws Exception {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    
    private String toJson(Object obj) throws Exception{
        return new ObjectMapper().writeValueAsString(obj);
    }

    private ImmutableMap.Builder mapBuilder() {
        return ImmutableMap.<String, String>builder();
    }
}
