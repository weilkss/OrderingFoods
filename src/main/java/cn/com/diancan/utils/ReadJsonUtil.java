package cn.com.diancan.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

@Component
public class ReadJsonUtil {
    /**
     * 加载本地静态资源json-已废弃
     * @return
     * @throws IOException
     */
    public JSONObject startStreamTask() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("static/foods.json");
        String str = IOUtils.toString(new InputStreamReader(classPathResource.getInputStream(), "UTF-8"));

        JSONObject object = JSONObject.parseObject(str);

        return object;
    }
}